package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeypressed;
    GamePanel gp;
    float volume = 0.2F;
    StringBuilder playername = new StringBuilder();

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (gp.gameState == gp.inputplayernamestate) {
            char c = e.getKeyChar();

            // backspace key
            if(c == '\b'){
                if(!playername.isEmpty()){
                    playername.deleteCharAt(playername.length()-1);
                }
                return;
            }

            // enter key
            if (c == '\n') {
                gp.player.playername = playername.toString();
                return;
            }

            // length limit
            if(gp.player.playername.length() >= 20) return;

            // append printable char
            if(!Character.isISOControl(c)) {
                playername.append((c));
            }

            gp.player.playername = playername.toString();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if (gp.gameState == gp.titleState) {
            try { handleTitlestate(code); } catch (Exception ex) { ex.printStackTrace(); }
            return;
        }

        if (gp.gameState == gp.gameWinstate) {
            handlegameWin(code);
            return;
        }

        if (gp.gameState == gp.endingCreditState) {
            handleEndingCreditState(code);
            return;
        }

        if (gp.gameState == gp.startingcreditState) {
            handleCreditState(code);
            return;
        }

        if (gp.gameState == gp.inputplayernamestate) {
            handleNameInputState(code);
            return;
        }

        if (gp.gameState == gp.playState) {
            handlePlaystate(code);
            // no return — allow global keys like P
        }

        else if (gp.gameState == gp.characterstate) {
            handleCharacterstate(code);
            return;
        }

        else if (gp.gameState == gp.pauseState) {
            handlePausestate(code);
            return;
        }

        else if (gp.gameState == gp.optionState) {
            handleOptionstate(code);
            return;
        }

        else if (gp.gameState == gp.gameoverstate) {
            handlegameOver(code);
            return;
        }

        else if (gp.gameState == gp.scoreBoardState) {
            handleScoreboardState(code);
            return;
        }

        else if (gp.gameState == gp.dialogueState) {
            handleDialoguestate(code);
            return;
        }

        if (code == KeyEvent.VK_P &&
                (gp.gameState == gp.playState || gp.gameState == gp.pauseState)) {

            gp.playSE(1, volume);
            gp.gameState = (gp.gameState == gp.playState)
                    ? gp.pauseState
                    : gp.playState;
        }
    }

    private void handleCharacterstate(int asciiCode) {
        if (asciiCode == KeyEvent.VK_C) {
            gp.gameState = gp.playState;
        } else if (asciiCode == KeyEvent.VK_W) {
            if (gp.ui.slotrow != 0) {
                gp.ui.slotrow--;
            }

        } else if (asciiCode == KeyEvent.VK_A) {
            if (gp.ui.slotcol != 0) {
                gp.ui.slotcol--;
            }
        } else if (asciiCode == KeyEvent.VK_S) {
            if (gp.ui.slotrow != 3) {
                gp.ui.slotrow++;
            }
        } else if (asciiCode == KeyEvent.VK_D) {
            if (gp.ui.slotcol != 4) {
                gp.ui.slotcol++;
            }
        }
    }

    private void handleDialoguestate(int asciiCode) {
        if (asciiCode == KeyEvent.VK_ENTER) {
            gp.gameState = gp.playState;
        }
    }

    private void handleOptionstate(int asciiCode) {
        if (gp.ui.showControlsWindow) {
            // Handle ONLY controls subwindow navigation
            if (asciiCode == KeyEvent.VK_ENTER && gp.ui.commandNum == 0) {
                gp.playSE(1, volume);
                gp.ui.showControlsWindow = false; // close subwindow
                gp.ui.commandNum = 2; // return cursor to "Controls" in options menu
            }

        } else {
            // Normal options menu
            if (asciiCode == KeyEvent.VK_W || asciiCode == KeyEvent.VK_UP) {
                gp.playSE(1, volume);
                gp.ui.commandNum = gp.ui.commandNum <= 0 ? gp.ui.optionsItems.length - 1 : --gp.ui.commandNum;
            } else if (asciiCode == KeyEvent.VK_S || asciiCode == KeyEvent.VK_DOWN) {
                gp.playSE(1, volume);
                gp.ui.commandNum = gp.ui.commandNum > gp.ui.optionsItems.length - 2 ? 0 : ++gp.ui.commandNum;
            } else if (asciiCode == KeyEvent.VK_ENTER) {
                gp.playSE(1, volume);

                if (gp.ui.commandNum == 0) {
                    // Volume
                } else if (gp.ui.commandNum == 1) {
                    // Toggle SE
                    gp.ui.toggleSe();
                } else if (gp.ui.commandNum == 2) {
                    // Show controls subwindow
                    gp.playSE(1, volume);
                    gp.ui.showControlsWindow = true;
                    gp.ui.commandNum = 0; // back button inside controls
                } else if (gp.ui.commandNum == 3) {
                    // Back
                    gp.config.saveConfig();
                    gp.gameState = gp.previousState;
                    gp.ui.commandNum = 0;
                }
            }

            // Volume adjustment with arrows
            if (gp.ui.commandNum == 0) {
                if (asciiCode == KeyEvent.VK_RIGHT) {
                    gp.playSE(1, volume);
                    gp.ui.increseVolume();
                } else if (asciiCode == KeyEvent.VK_LEFT) {
                    gp.playSE(1, volume);
                    gp.ui.decreseVolume();
                }
            }
        }
    }

    private void handlePausestate(int asciiCode) {
        if (asciiCode == KeyEvent.VK_W || asciiCode == KeyEvent.VK_UP) {
            gp.playSE(1, volume);
            gp.ui.commandNum = gp.ui.commandNum <= 0 ? gp.ui.pauseItems.length - 1 : --gp.ui.commandNum;
        }


        if (asciiCode == KeyEvent.VK_S || asciiCode == KeyEvent.VK_DOWN) {
            gp.playSE(1, volume);
            gp.ui.commandNum = gp.ui.commandNum > gp.ui.pauseItems.length - 2 ? 0 : ++gp.ui.commandNum;
        }

        if (asciiCode == KeyEvent.VK_ENTER) {
            gp.playSE(1, volume);
            if (gp.ui.commandNum == 0) {
                // Enter Resume Game
                gp.gameState = gp.playState;
                gp.ui.commandNum = 0; // reset when entering play
            } else if (gp.ui.commandNum == 1) {
                // save game
                gp.ui.showsavemsg();
                gp.dataStorage.saveData();
                gp.ui.gamesaved = true;
                gp.ui.gameDeleted = false;
            } else if (gp.ui.commandNum == 2) {
                // Options
                gp.previousState = gp.gameState;
                gp.gameState = gp.optionState;
                gp.ui.commandNum = 0; // reset when entering play

            } else {
                // Exit the game
                gp.gameState = gp.titleState;
                gp.ui.commandNum = 0; // reset when entering play

            }
        }
    }

    private void handleCreditState(int asciiCode) {
        if (asciiCode == KeyEvent.VK_ENTER || asciiCode == KeyEvent.VK_ESCAPE) {
            gp.playSE(1, volume);
            gp.gameState = gp.titleState;  // Go to title screen
        }
    }

    private void handleEndingCreditState(int asciiCode) {
        if (asciiCode == KeyEvent.VK_ENTER || asciiCode == KeyEvent.VK_ESCAPE) {
            gp.playSE(1, volume);
            gp.gameState = gp.titleState;
            gp.ui.endingScrollY = gp.scrHeight; // Reset scroll position
            gp.ui.endingCreditTimer = 0;
        }
    }

    private void handlegameWin(int asciiCode) {
        if (asciiCode == KeyEvent.VK_ENTER) {
            gp.playSE(1, volume);
            gp.gameState = gp.endingCreditState;

        }
    }

    private void handlePlaystate(int asciiCode) {
        if (asciiCode == KeyEvent.VK_W || asciiCode == KeyEvent.VK_UP) upPressed = true;
        if (asciiCode == KeyEvent.VK_S || asciiCode == KeyEvent.VK_DOWN) downPressed = true;
        if (asciiCode == KeyEvent.VK_A || asciiCode == KeyEvent.VK_LEFT) leftPressed = true;
        if (asciiCode == KeyEvent.VK_D || asciiCode == KeyEvent.VK_RIGHT) rightPressed = true;
        if (asciiCode == KeyEvent.VK_ENTER) enterPressed = true;
        if (asciiCode == KeyEvent.VK_X && gp.player.level >= 5) shotKeypressed = true;
        if (asciiCode == KeyEvent.VK_C) {
            gp.gameState = gp.characterstate;
        }

    }

    private void handleTitlestate(int asciiCode) throws InterruptedException {

        if (asciiCode == KeyEvent.VK_W || asciiCode == KeyEvent.VK_UP) {
            gp.playSE(1, volume);
            gp.ui.commandNum = gp.ui.commandNum <= 0 ? gp.ui.menuItems.length - 1 : --gp.ui.commandNum;
        }


        if (asciiCode == KeyEvent.VK_S || asciiCode == KeyEvent.VK_DOWN) {
            gp.playSE(1, volume);
            gp.ui.commandNum = gp.ui.commandNum > gp.ui.menuItems.length - 2 ? 0 : ++gp.ui.commandNum;
        }

        if (asciiCode == KeyEvent.VK_ENTER) {
            gp.playSE(1, volume);
            if (gp.ui.commandNum == 0) {
                gp.restart();
                gp.gameState = gp.inputplayernamestate;

            }
            else if (gp.ui.commandNum == 1) {
                String saveDir = System.getProperty("user.home") + "/.rezero-game/";
                File saveFile = new File(saveDir + "saveData.txt");
                gp.ui.gameDeleted = false;

                if (saveFile.exists()) {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(saveFile));
                        String firstLine = br.readLine();
                        br.close();

                        // Check if first line is "1" (valid save)
                        if (firstLine != null && firstLine.trim().equals("0")) {
                            gp.ui.gameDeleted = true;
                        }
                    } catch (Exception e) {
                        gp.ui.gameDeleted = false;
                    }
                } else {
                    gp.ui.gameDeleted = true;
                }

                if (!gp.ui.gameDeleted) {
                    // Load the saved game
                    gp.dataStorage.readData();
                    gp.dataStorage.applyToPlayer(gp.player);
                    gp.gameState = gp.playState;
                } else {
                    // No valid save - show message
                    gp.ui.shownosavemsg();
                }
            }else if (gp.ui.commandNum == 2) {
                // delete save data
                gp.ui.showdeletemsg();
                gp.dataStorage.deleteData();
                gp.ui.gameDeleted = true;
                gp.ui.gamesaved = false;
                // we can give a popup confirmation or not here
            } else if (gp.ui.commandNum == 3) {
                // scoreboard
                gp.previousState = gp.gameState;
                gp.gameState = gp.scoreBoardState;
                gp.ui.commandNum = 0; // reset when entering play

            } else if (gp.ui.commandNum == 4) {
                // option
                gp.previousState = gp.gameState;
                gp.gameState = gp.optionState;
                gp.ui.commandNum = 0; // reset when entering play

            }
            else { //exit
//                gp.dataStorage.saveData();
                System.exit(0);
            }
        }
    }

    private void handleNameInputState(int asciiCode) {
        if (asciiCode == KeyEvent.VK_ENTER) {
            if (!gp.player.playername.trim().isEmpty()) { // Make sure name is not empty
                gp.playSE(1, volume);
                gp.gameState = gp.playState; // Start the game
//                gp.playMusic(0); // Start game music
                gp.ui.commandNum = 0; // reset command
            }
        }

        if (asciiCode == KeyEvent.VK_BACK_SPACE) {
            if (!gp.player.playername.isEmpty()) {
                gp.player.playername = gp.player.playername.substring(0, gp.player.playername.length() - 1);
            }
        }

        if (asciiCode == KeyEvent.VK_ESCAPE) {
            // Allow player to go back to title screen
            gp.gameState = gp.titleState;
            gp.ui.commandNum = 0;
        }
    }

    private void handlegameOver(int code) {

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.playSE(1, volume);
            gp.ui.commandNum = gp.ui.commandNum <= 0 ? 1 : --gp.ui.commandNum;
        }


        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.playSE(1, volume);
            gp.ui.commandNum = gp.ui.commandNum >= 1 ? 0 : ++gp.ui.commandNum;
        }

        if (code == KeyEvent.VK_ENTER) {
            gp.playSE(1, volume);
            if (gp.ui.commandNum == 0) {
                // Retry
                gp.retry();
                gp.gameState = gp.playState;
                gp.playSE(7, 1f);

                gp.ui.commandNum = 0; // reset when entering play
            } else if (gp.ui.commandNum == 1) {
                // Exit
                gp.restart();
                gp.gameState = gp.titleState;
                gp.ui.commandNum = 0; // reset when entering play

            }

        }
    }

    private void handleScoreboardState(int asciiCode){
        if(asciiCode == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.titleState;
            gp.ui.commandNum = 0;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int asciiCode = e.getKeyCode();

        if (asciiCode == KeyEvent.VK_W || asciiCode == KeyEvent.VK_UP) upPressed = false;
        if (asciiCode == KeyEvent.VK_S || asciiCode == KeyEvent.VK_DOWN) downPressed = false;
        if (asciiCode == KeyEvent.VK_A || asciiCode == KeyEvent.VK_LEFT) leftPressed = false;
        if (asciiCode == KeyEvent.VK_D || asciiCode == KeyEvent.VK_RIGHT) rightPressed = false;
        if (asciiCode == KeyEvent.VK_X) shotKeypressed = false;
    }
}