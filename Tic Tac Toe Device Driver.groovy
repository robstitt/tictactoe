/*  ****************  Tic Tac Toe Device Driver  ****************
 *
 *  importUrl: https://github.com/robstitt/Hubitat-TicTacToe-Device/raw/main/Tic-Tac-Toe%20Virtual%20Device%20Driver.groovy
 *
 *  Copyright 2024 Robert L. Stitt
 *
 *-------------------------------------------------------------------------------------------------------------------
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  The grid consists of columns A, B, and C and rows 1, 2, and 3 (noted here as: A1, A2, A3, B1, B2, B3, C1, C2, C3).
 *
 *  For the sake of using a button push to set the squares, these correspond to 1, 2, 3, 4, 5, 6, 7, 8, 9 (in that order)
 *
 *  The Undo function is also triggered by a "Hold" on button number 39
 *
 *  The Reset function is also triggered by a "Push on button number 39
 *
 *
 * Created: 02/16/2024
 *
 * Last Update: mm/dd/yyyy
 *    - (no updates yet)
 */

metadata {
   definition (
      name: "Tic Tac Toe Virtual Device",
      namespace: "robstitt",
      author: "Robert L. Stitt"
      //importUrl: "https://github.com/robstitt/Hubitat-TicTacToe-Device/raw/main/Tic-Tac-Toe%20Virtual%20Device%20Driver.groovy"
       ) {
      command "initialize"
      command "reset"
      command "undo"

      capability "Actuator" 
      capability "PushableButton"
      capability "HoldableButton"
      capability "ReleasableButton"

      attribute "A1", "string"
      attribute "A2", "string"
      attribute "A3", "string"
      attribute "B1", "string"
      attribute "B2", "string"
      attribute "B3", "string"
      attribute "C1", "string"
      attribute "C2", "string"
      attribute "C3", "string"

      attribute "Winner", "string"

      attribute "Player", "string"

      attribute "Message", "string"
      }

   preferences() {
      input("logEnable", "bool", title: "Enable logging", required: true, defaultValue: false)
   }
}

def initialize() {
   if(logEnable) log.info "Tic Tac Toe Device Driver Initializing."
   sendEvent(name: "numberOfButtons", value: 39)
   reset()
}

def updated() {
   if(logEnable) log.info "Tic Tac Toe Device Driver Updated."
   sendEvent(name: "numberOfButtons", value: 39)
   reset()
}

def installed(){
   if(logEnable) log.info "Tic Tac Toe Device has been Installed."
   sendEvent(name: "numberOfButtons", value: 39)
   reset()
}

void logsOff(){
   log.warn "Debug logging disabled."
   device.updateSetting("logEnable",[value:"false",type:"bool"])
}

def reset() {
    if(logEnable) log.info "Resetting the Tic Tac Toe Board"
    
    state.sA1="-"
    state.sA2="-"
    state.sA3="-"

    state.sB1="-" 
    state.sB2="-"
    state.sB3="-"

    state.sC1="-" 
    state.sC2="-"
    state.sC3="-"

    sendEvent(name: "A1", value: state.sA1, displayed: true)
    sendEvent(name: "A2", value: state.sA2, displayed: true)
    sendEvent(name: "A3", value: state.sA3, displayed: true)
    sendEvent(name: "B1", value: state.sB1, displayed: true)
    sendEvent(name: "B2", value: state.sB2, displayed: true)
    sendEvent(name: "B3", value: state.sB3, displayed: true)
    sendEvent(name: "C1", value: state.sC1, displayed: true)
    sendEvent(name: "C2", value: state.sC2, displayed: true)
    sendEvent(name: "C3", value: state.sC3, displayed: true)
    
    state.sPlayer="X"
    sendEvent(name: "Player", value: state.sPlayer, displayed: true)

    state.sWinner="-"
    sendEvent(name: "Winner", value: state.sWinner, displayed: true)
    sendEvent(name: "Message", value: "Welcome to Tic Tac Toe!", displayed: true)   
 
    state.lastSquareSet = 0
    state.gameOver = false
}

def togglePlayer() {
    if (state.gameOver) {
        if (logEnable) log.info "The game is already over-ignoring the toggle player request"
    } else {
        if (state.sPlayer == "X") {
            state.sPlayer="O"
         } else {
            state.sPlayer="X"
         }
         sendEvent(name: "Player", value: state.sPlayer)
    }
}

def undo() {
  if ((state.lastSquareSet>0) && (!state.gameOver)) {
      if (logEnable) log.info "Undoing the move in square ${state.lastSquareSet}" 

      hold(state.lastSquareSet)
      state.lastSquareSet=0
      togglePlayer() 
  } else {
      if (logEnable) log.info "There is no action to undo" 
      sendEvent(name: "Message", value: "No action to undo")
  }
}

def checkBoard() { 
    if (logEnable) {  
        log.info device.displayName + "Row 3 is >${state.sC1}|${state.sC2}|${state.sC3}<"
        log.info device.displayName + "Row 2 is >${state.sB1}|${state.sB2}|${state.sB3}<"
        log.info device.displayName + "Row 1 is >${state.sA1}|${state.sA2}|${state.sA3}<"
    }
    
    if ((state.sA1 != "-") && (state.sA1 == state.sA2) && (state.sA1 == state.sA3)) {
        if (logEnable) log.info "${device.displayName} Row A is a match"
        state.gameOver=true
        state.sWinner = state.sA1
        sendEvent(name: "Winner", value: state.sA1)
    } else if ((state.sB1 != "-") && (state.sB1 == state.sB2) && (state.sB1 == state.sB3)) {
        if (logEnable) log.info "${device.displayName} Row B is a match"
        state.gameOver=true
        state.sWinner = state.sB1
        sendEvent(name: "Winner", value: state.sB1)
    } else if ((state.sC1 != "-") && (state.sC1 == state.sC2) && (state.sC1 == state.sC3)) {
        if (logEnable) log.info "${device.displayName} Row C is a match"
        state.gameOver=true
        state.sWinner = state.sC1
        sendEvent(name: "Winner", value: state.sC1)
    } else if ((state.sA1 != "-") && (state.sA1 == state.sB1) && (state.sA1 == state.sC1)) {
        if (logEnable) log.info "${device.displayName} Column 1 is a match"
        state.gameOver=true
        state.sWinner = state.sA1
        sendEvent(name: "Winner", value: state.sA1)
    } else if ((state.sA2 != "-") && (state.sA2 == state.sB2) && (state.sA2 == state.sC2)) {
        if (logEnable) log.info "${device.displayName} Column 2 is a match"
        state.gameOver=true
        state.sWinner = state.sA2
        sendEvent(name: "Winner", value: state.sA2)
    } else if ((state.sA3 != "-") && (state.sA3 == state.sB3) && (state.sA3 == state.sC3)) {
        if (logEnable) log.info "${device.displayName} Column 3 is a match"
        state.gameOver=true
        state.sWinner = state.sA3
        sendEvent(name: "Winner", value: state.sA3)
    } else if ((state.sA1 != "-") && (state.sA1 == state.sB2) && (state.sA1 == state.sC3)) {
        if (logEnable) log.info "${device.displayName} Diagonal top-left to bottom-right is a match"
        state.gameOver=true
        state.sWinner = state.sA1
        sendEvent(name: "Winner", value: state.sA1)
    } else if ((state.sA3 != "-") && (state.sA3 == state.sB2) && (state.sA3 == state.sC1)) {
        if (logEnable) log.info "${device.displayName} Diagonal bottom-left to top-right is a match"
        state.gameOver=true
        state.sWinner = state.sA3
        sendEvent(name: "Winner", value: state.sA3)
    }  else {
        if (logEnable) log.info "${device.displayName} No winner yet"
        sendEvent(name: "Message", value: "No winner yet...")
    }     

  if (state.gameOver) {
      if (logEnable) log.info "${device.displayName} Game Over-Winner is ${state.sWinner}"
      sendEvent(name: "Message", value: "Game Over! The winner is ${state.sWinner} (press \"Reset\" to start over)")
  }  
}

void push(sbutton) {
    int button = sbutton as Integer
    
    if (logEnable) log.info "${device.displayName} Button ${button} was pushed for player ${state.sPlayer}"

    if (button == 39) {
        if (logEnable) log.info "${device.displayName} Reset function called via push on button 39"
        reset()
    } else if (state.gameOver) {
       if (logEnable) log.info "The game is already over-ignoring the button push"    
    } else {
       switch (button) {
           case 1:
               if (state.sA1 == "-") { 
                   state.sA1=state.sPlayer
                   sendEvent(name:"A1", value: "${state.sPlayer}") 
                   state.lastSquareSet=button
                   togglePlayer() 
               } else { 
                   sendEvent(name:"Message", value: "Square A1 already occupied") 
               }
               break
           case 2:
               if (state.sA2 == "-") { 
                   state.sA2=state.sPlayer
                   sendEvent(name:"A2", value: "${state.sPlayer}")  
                   state.lastSquareSet=button
                   togglePlayer() 
               } else { 
                   sendEvent(name:"Message", value: "Square A2 already occupied") 
               }
               break
           case 3:
               if (state.sA3 == "-") { 
                   state.sA3=state.sPlayer
                   sendEvent(name:"A3", value: "${state.sPlayer}")  
                   state.lastSquareSet=button
                   togglePlayer() 
               } else { 
                   sendEvent(name:"Message", value: "Square A3 already occupied") 
               }
               break
           case 4:
               if (state.sB1 == "-") { 
                   state.sB1=state.sPlayer
                   sendEvent(name:"B1", value: "${state.sPlayer}")  
                   state.lastSquareSet=button
                   togglePlayer() 
               } else { 
                   sendEvent(name:"Message", value: "Square B1 already occupied") 
               }
               break
           case 5:
               if (state.sB2 == "-") { 
                   state.sB2=state.sPlayer
                   sendEvent(name:"B2", value: "${state.sPlayer}")  
                   state.lastSquareSet=button
                   togglePlayer() 
               } else { 
                   sendEvent(name:"Message", value: "Square B2 already occupied") 
               }
               break
           case 6:
               if (state.sB3 == "-") { 
                   state.sB3=state.sPlayer
                   sendEvent(name:"B3", value: "${state.sPlayer}")  
                   state.lastSquareSet=button
                   togglePlayer() 
               } else { 
                   sendEvent(name:"Message", value: "Square B3 already occupied") 
               }
               break
           case 7:
               if (state.sC1 == "-") { 
                   state.sC1=state.sPlayer
                   sendEvent(name:"C1", value: "${state.sPlayer}")  
                   state.lastSquareSet=button
                   togglePlayer() 
               } else { 
                   sendEvent(name:"Message", value: "Square C1 already occupied") 
               }
               break
           case 8:
               if (state.sC2 == "-") { 
                   state.sC2=state.sPlayer
                   sendEvent(name:"C2", value: "${state.sPlayer}")  
                   state.lastSquareSet=button
                   togglePlayer() 
               } else { 
                   sendEvent(name:"Message", value: "Square C2 already occupied") 
               }
               break
           case 9:
               if (state.sC3 == "-") { 
                   state.sC3=state.sPlayer
                   sendEvent(name:"C3", value: "${state.sPlayer}")  
                   state.lastSquareSet=button
                   togglePlayer() 
               } else { 
                   sendEvent(name:"Message", value: "Square C3 already occupied") 
               }
               break
           default:       //undefined button number
               log.warn "${device.displayName} Invalid Button Number (1-9 only)=${button} pushed"
               break
       }
    }
    if (button != "39") checkBoard()
}

void hold(sbutton) {
    int button = sbutton as Integer    
    
    if (logEnable) log.info "${device.displayName} Button ${button} was held"

    if (button == 39) {
        if (logEnable) log.info "${device.displayName} Undo function called via hold on button 39"     
        undo()
    } else if (state.gameOver) {
       if (logEnable) log.info "The game is already over-ignoring the button hold"    
    } else {
       switch (button) {
           case 1:
               if (state.sA1 != "-") { 
                   state.sA1 = "-"
                   sendEvent(name:"A1", value: state.sA1) 
               } else { 
                   sendEvent(name:"Message", value: "Square A1 not currently occupied") 
               }
               break
           case 2:
               if (state.sA2 != "-") { 
                   state.sA2 = "-"
                   sendEvent(name:"A2", value: state.sA2) 
               } else { 
                   sendEvent(name:"Message", value: "Square A2 not currently occupied") 
               }
               break
           case 3:
               if (state.sA3 != "-") { 
                   state.sA3 = "-"
                   sendEvent(name:"A3", value: state.sA3) 
               } else { 
                   sendEvent(name:"Message", value: "Square A3 not currently occupied") 
               }
               break
           case 4:
               if (state.sB1 != "-") { 
                   state.sB1 = "-"
                   sendEvent(name:"B1", value: state.sB1) 
               } else { 
                   sendEvent(name:"Message", value: "Square B1 not currently occupied") 
               }
               break
           case 5:
               if (state.sB2 != "-") { 
                   state.sB2 = "-"
                   sendEvent(name:"B2", value: state.sB2) 
               } else { 
                   sendEvent(name:"Message", value: "Square B2 not currently occupied") 
               }
               break
           case 6:
               if (state.sB3 != "-") { 
                   state.sB3 = "-"
                   sendEvent(name:"B3", value: state.sB3) 
               } else { 
                   sendEvent(name:"Message", value: "Square B3 not currently occupied")
               }
               break
           case 7:
               if (state.sC1 != "-") { 
                   state.sC1 = "-"
                   sendEvent(name:"C1", value: state.sC1) 
               } else { 
                   sendEvent(name:"Message", value: "Square C1 not currently occupied")
               }
               break
           case 8:
               if (state.sC2 != "-") { 
                   state.sC2 = "-"
                   sendEvent(name:"C2", value: state.sC2)
               } else { 
                   sendEvent(name:"Message", value: "Square C2 not currently occupied") 
               }
               break
           case 9:
               if (state.sC3 != "-") { 
                   state.sC3 = "-"
                   sendEvent(name:"C3", value: state.sC3) 
               } else { 
                   sendEvent(name:"Message", value: "Square C3 not currently occupied") 
               }
               break
           default:       //undefined button number
               log.warn "${device.displayName} Invalid Button Number (1-9 only)=${button} held"
               break
       }
    }
    checkBoard()
}


void release(button) {
  if (logEnable) log.info "${device.displayName} Ignoring release of button=$button" 
}

