# tictactoe
Tic Tac Toe Driver For Hubitat

This driver is intended to be assigned to a manually created "virtual device" on the Hubitat Home Automation platform. This "Tic-Tac-Toe" device can be with a dashboard and Rule Manager rules/scripts to allow playing a Tic-Tac-Toe game. 

If desired, physical, smart home RGBW bulbs (or other desired smart home devices) can controlled using Rule Manager rules/scripts to create a visual Tic-Tac-Toe display board. 

Note that any dashboards, Rule Manager rules/scripts, etc. are NOT included with this driver and are NOT supported by the author of this driver.

This driver SOLELY intended for entertainment purposes ONLY.

Notes:  

Upon installation, press the "Initialize" button on the virtual device's Commands tab to ensure that it is properly installed and initialized (this ensures the proper number of buttons is defined, etc.) and sets the State values.

The Tic Tac Toe grid consists of columns A, B, and C and rows 1, 2, and 3 (referenced as attributes: A1, A2, A3, B1, B2, B3, C1, C2, C3). The initial value for each square in the grid is set to "-" (meaning it has not yet been selected). As each player selects squares, they will be set to "X" or "O" depending on which player selected the square.

The following additional attributes are defined:

- Message: A text field containing the current state of the game
- numberOfButtons: This is the number of buttons defined for the device, but meant for internal use only
- Player: The current player (X or O)
- Winner: When a winner is detected, this will be "X" or "O". Until then, it is a "-" to indicate no winner yet.

The driver automatically starts the game with the "X" player, automatically alternating between "X" and "O". To select a square as the current player, initiate a button press on the virtual device (e.g., "push button 1" to select square A1).  The button numbers for each square are A1=1, A2=2, A3=3, B1=4, B2=5, B3=6, C1=7, C2=8, C3=9.

To "undo" the previous move, initiate a "hold button 39" on the virtual device or initiate an "undo" command on the virtual device. Note that performing a "hold" on buttons 1-9 will deselect the corresponding square, but this is not normally done in a Tic-Tac-Toe game.

To "reset" the game, initiate a "push button 39" on the virtual device or initiate a "reset" command on the virtual device.



=========

NOTICE ABOUT WARRANTIES AND LIABILITY:

By using this app, driver, or any related code in any manner whatsoever, you fully and completely agree to the following:

YOU take FULL AND COMPLETE responsibility for ANY AND ALL use of this app and related code and agree that ALL responsibility and liability for ANY use you make of the app or related code will be TOTALLY as if YOU developed this ENTIRELY ON YOUR OWN.

This app and related code is being provided freely for anyone's use with absolutely NO GUARANTEES, NO WARRANTIES, NO LIABILITY ASSUMED, NO RESPONSIBIILITY whatsoever.

There are no guarantees, including but not limited to: no guarantees that it will work, no guarantees that it will work properly when desired/necessary, no guarantees that it will be bug free, no guarantees that it will not cause impact or harm to your hub or any other device. This app and related code are provided solely under the condition that they are TOTALLY USE AT YOUR OWN RISK.

There is NO GUARANTEE, NO LIABILITY, NO RESPONSIBILITY assumed for providing ANY future updates to this app nor to any of the related code.

If these terms are not acceptable to you or otherwise not allowed (e.g., by your location, etc.), then you SHALL NOT MAKE ANY USE OF THIS APP OR RELATED CODE IN ANY MANNER.

=========

LICENSING NOTE

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
in compliance with the License. You may obtain a copy of the License at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
for the specific language governing permissions and limitations under the License.