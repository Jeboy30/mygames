import tkinter as tk
from tkinter import messagebox
import pygame
import os

class TicTacToeGame(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("Tic-Tac-Toe Clash Duel")
        self.geometry("700x700")
        self.configure(bg="#00ced1")

        pygame.mixer.init()
        self.current_player = "X"
        self.playerXScore = 0
        self.playerOScore = 0
        self.buttons = [None] * 9

        self.show_start_menu()

    def play_background_music(self, file_path):
        if os.path.exists(file_path):
            pygame.mixer.music.load(file_path)
            pygame.mixer.music.play(-1)
        else:
            print("Background music file not found")

    def stop_background_music(self):
        pygame.mixer.music.stop()

    def play_sound_effect(self, file_path):
        if os.path.exists(file_path):
            sound = pygame.mixer.Sound(file_path)
            sound.play()
        else:
            print("Sound effect file not found")

    def show_start_menu(self):
        self.main_panel = tk.Frame(self, bg="#00ced1")
        self.main_panel.pack(expand=True, fill='both')

        title_label = tk.Label(self.main_panel, text="Tic-Tac-Toe Clash Duel", font=("Segoe UI", 48, "bold"), fg="white", bg="#00ced1")
        title_label.pack(pady=(50, 20))

        start_button = tk.Button(self.main_panel, text="START", font=("Tahoma", 30, "bold"), bg="#99ff99", command=self.show_player_selection)
        start_button.pack(pady=10)

        exit_button = tk.Button(self.main_panel, text="EXIT", font=("Tahoma", 30, "bold"), bg="#ff9999", command=self.quit)
        exit_button.pack(pady=10)

    def show_player_selection(self):
        self.main_panel.destroy()

        self.main_panel = tk.Frame(self, bg="#00ced1")
        self.main_panel.pack(expand=True, fill='both')

        player_label = tk.Label(self.main_panel, text="Select First Player", font=("Segoe UI", 50, "bold"), fg="white", bg="#00ced1")
        player_label.pack(pady=(50, 20))

        playerX_button = tk.Button(self.main_panel, text="Player X", font=("Tahoma", 30, "bold"), bg="#ff9999", command=lambda: self.start_game("X"))
        playerX_button.pack(pady=10)

        playerO_button = tk.Button(self.main_panel, text="Player O", font=("Tahoma", 30, "bold"), bg="#9999ff", command=lambda: self.start_game("O"))
        playerO_button.pack(pady=10)

        exit_button = tk.Button(self.main_panel, text="EXIT", font=("Tahoma", 24, "bold"), bg="#ff9999", command=self.quit)
        exit_button.pack(side='bottom', pady=20)

    def start_game(self, first_player):
        self.current_player = first_player
        self.main_panel.destroy()
        self.init_game_components()

    def init_game_components(self):
        self.game_panel = tk.Frame(self)
        self.game_panel.pack(expand=True, fill='both', side='left')  # Set side to 'left' to place the game panel on the left side

        for i in range(9):
            button = tk.Button(self.game_panel, text="", font=("Segoe UI", 48, "bold"), width=5, height=2, command=lambda i=i: self.on_button_click(i))
            button.grid(row=i//3, column=i%3)
            self.buttons[i] = button

        self.control_panel = tk.Frame(self)
        self.control_panel.pack(fill='x', side='bottom')

        exit_button = tk.Button(self.control_panel, text="EXIT", font=("Tahoma", 24, "bold"), bg="#ff9999", command=self.quit)
        exit_button.pack(side='right', padx=10, pady=20)

        self.score_panel = tk.Frame(self, bd=2, relief="raised")
        self.score_panel.pack(fill='y', side='right')  # Set side to 'right' to place the score panel on the right side

        self.playerXLabel = tk.Label(self.score_panel, text="0", font=("Segoe UI", 24, "bold"))
        self.playerXLabel.grid(row=0, column=1)
        tk.Label(self.score_panel, text="Player X:", font=("Segoe UI", 24, "bold")).grid(row=0, column=0)

        self.playerOLabel = tk.Label(self.score_panel, text="0", font=("Segoe UI", 24, "bold"))
        self.playerOLabel.grid(row=1, column=1)
        tk.Label(self.score_panel, text="Player O:", font=("Segoe UI", 24, "bold")).grid(row=1, column=0)

    def on_button_click(self, index):
        button = self.buttons[index]
        if button["text"] == "":
            button["text"] = self.current_player
            button.config(state="disabled", bg="#ff9999" if self.current_player == "X" else "#9999ff")
            self.play_sound_effect("C:\\Users\\admin\Desktop\\Finalproject_games\\mygames\\python\\music\\click.mp3")
            if self.check_for_win():
                self.update_score(self.current_player)
                messagebox.showinfo("Game Over", f"Player {self.current_player} wins!")
                self.reset_game()
            elif self.is_board_full():
                messagebox.showinfo("Game Over", "The game is a draw!")
                self.reset_game()
            else:
                self.current_player = "O" if self.current_player == "X" else "X"

    def check_for_win(self):
        board = [button["text"] for button in self.buttons]
        win_conditions = [(0, 1, 2), (3, 4, 5), (6, 7, 8), (0, 3, 6), (1, 4, 7), (2, 5, 8), (0, 4, 8), (2, 4, 6)]
        for condition in win_conditions:
            if board[condition[0]] == board[condition[1]] == board[condition[2]] != "":
                return True
        return False

    def is_board_full(self):
        return all(button["text"] != "" for button in self.buttons)

    def update_score(self, winner):
        if winner == "X":
            self.playerXScore += 1
            self.playerXLabel["text"] = str(self.playerXScore)
        else:
            self.playerOScore += 1
            self.playerOLabel["text"] = str(self.playerOScore)

    def reset_game(self):
        for button in self.buttons:
            button.config(text="", state="normal", bg="SystemButtonFace")
        self.current_player = "X"
    # Adding an exit button as an alternative way to quit the application   

if __name__ == "__main__":
    # Ensure pygame mixer is initialized before creating the app
    pygame.mixer.init()
    app = TicTacToeGame()
    app.play_background_music("C:\\Users\\admin\Desktop\\Finalproject_games\\mygames\\python\\music\\background.mp3")
    app.mainloop()
