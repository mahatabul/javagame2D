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

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (gp.gameState == gp.inputplayernamestate) {
            char c = e.getKeyChar();

            // Allow letters, numbers, and spaces
//            if (Character.isLetterOrDigit(c) || c == ' ') {
                if (gp.player.playername.length() < 10) { // Max 15 characters
                    gp.player.playername += c;
                }
//            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int asciiCode = e.getKeyCode();

        // Title State
        if (gp.gameState == gp.titleState) {
            try {
                handleTitlestate(asciiCode);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        } else if (gp.gameState == gp.gameWinstate) {
            handlegameWin(asciiCode);
        } else if (gp.gameState == gp.endingCreditState) {
            handleEndingCreditState(asciiCode);
        } else if (gp.gameState == gp.startingcreditState) {
            handleCreditState(asciiCode);
        } else if (gp.gameState == gp.inputplayernamestate) {
            handleNameInputState(asciiCode);
        }

        // Play state
        else if (gp.gameState == gp.playState) {
            handlePlaystate(asciiCode);
        }

        // character state
        else if (gp.gameState == gp.characterstate) {
            handleCharacterstate(asciiCode);
        }
        // Pause State
        else if (gp.gameState == gp.pauseState) {
            handlePausestate(asciiCode);
        }
        // Option State
        else if (gp.gameState == gp.optionState) {
            handleOptionstate(asciiCode);
        }
        // Game Over State
        else if (gp.gameState == gp.gameoverstate) {
            handlegameOver(asciiCode);
        }
        // Dialogue State
        if (gp.gameState == gp.dialogueState) {
            handleDialoguestate(asciiCode);
        }

        if (asciiCode == KeyEvent.VK_P && (gp.gameState == gp.playState || gp.gameState == gp.pauseState)) {
            gp.playSE(1, volume);
            gp.gameState = (gp.gameState == gp.playState) ? gp.pauseState : gp.playState;
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

            } else if (gp.ui.commandNum == 3) {
                // Exit the game
                gp.gameState = gp.titleState;
                gp.ui.commandNum = 0; // reset when entering play

            } else {

                // empty
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
                // Enter New Game
//                gp.assetHandler.respawnMonster();  // the monster fail to respawn bug fix
//                gp.player.setDefaultValue();
                gp.restart();
                gp.gameState = gp.inputplayernamestate;

            } else if (gp.ui.commandNum == 1) {

                // Load game - check save file directly
                File saveFile = new File("res/saveData.txt");
                gp.ui.gameDeleted = false;
                // Load game
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
            } else if (gp.ui.commandNum == 2) {
                // delete save data
                gp.ui.showdeletemsg();
                gp.dataStorage.deleteData();
                gp.ui.gameDeleted = true;
                gp.ui.gamesaved = false;
                // we can give a popup confirmation or not here
            } else if (gp.ui.commandNum == 3) {
                // options
                gp.previousState = gp.gameState;
                gp.gameState = gp.optionState;
                gp.ui.commandNum = 0; // reset when entering play

            } else {
//                gp.dataStorage.saveData();
                System.exit(0);
            }
        }
    }

    private void handleNameInputState(int asciiCode) {
        if (asciiCode == KeyEvent.VK_ENTER) {
            if (gp.player.playername.trim().length() > 0) { // Make sure name is not empty
                gp.playSE(1, volume);
                gp.gameState = gp.playState; // Start the game
//                gp.playMusic(0); // Start game music
                gp.ui.commandNum = 0; // reset command
            }
        }

        if (asciiCode == KeyEvent.VK_BACK_SPACE) {
            if (gp.player.playername.length() > 0) {
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