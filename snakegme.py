import pygame
import sys
import random

pygame.init()

# Constants
SCREEN_WIDTH, SCREEN_HEIGHT = 640, 480
GRIDSIZE = 20
GRID_WIDTH = SCREEN_WIDTH // GRIDSIZE
GRID_HEIGHT = SCREEN_HEIGHT // GRIDSIZE
FPS = 10

# Colors
WHITE = (255, 255, 255)
GREEN = (0, 255, 0)
RED = (255, 0, 0)
BLACK = (0, 0, 0)
BLUE = (0, 0, 255)
YELLOW = (255, 255, 0)  # New color for food

# Direction vectors
UP = (0, -1)
DOWN = (0, 1)
LEFT = (-1, 0)
RIGHT = (1, 0)

# Set up display
screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT))
pygame.display.set_caption("Snake Game")
clock = pygame.time.Clock()

# Load background image
background_image = None
try:
    background_image = pygame.image.load("C:\\Users\\Jezza\\OneDrive\\Desktop\\All Folder Files\\mygame\\snake.png")  # Ensure the image path is correct
    background_image = pygame.transform.scale(background_image, (SCREEN_WIDTH, SCREEN_HEIGHT))
except pygame.error as e:
    print(f"Unable to load background image: {e}")
    print("Proceeding without background image.")

# Snake eyes
eye_offset = GRIDSIZE // 3
eye_radius = 3
eye_color = WHITE

def draw_grid():
    for y in range(0, int(GRID_HEIGHT)):
        for x in range(0, int(GRID_WIDTH)):
            rect = pygame.Rect(x * GRIDSIZE, y * GRIDSIZE, GRIDSIZE, GRIDSIZE)
            pygame.draw.rect(screen, BLACK, rect)

def draw_snake(snake):
    for i, segment in enumerate(snake):
        rect = pygame.Rect(segment[0] * GRIDSIZE, segment[1] * GRIDSIZE, GRIDSIZE, GRIDSIZE)
        pygame.draw.rect(screen, GREEN, rect)
        if i == 0:  # Draw eyes on the head
            draw_snake_eyes(segment)

def draw_snake_eyes(segment):
    eye1_pos = (segment[0] * GRIDSIZE + eye_offset, segment[1] * GRIDSIZE + eye_offset)
    eye2_pos = (segment[0] * GRIDSIZE + GRIDSIZE - eye_offset, segment[1] * GRIDSIZE + eye_offset)
    pygame.draw.circle(screen, eye_color, eye1_pos, eye_radius)
    pygame.draw.circle(screen, eye_color, eye2_pos, eye_radius)

def draw_food(food):
    rect = pygame.Rect(food[0] * GRIDSIZE, food[1] * GRIDSIZE, GRIDSIZE, GRIDSIZE)
    pygame.draw.rect(screen, YELLOW, rect)

def draw_button(text, x, y, w, h, inactive_color, active_color, action=None):
    mouse = pygame.mouse.get_pos()
    click = pygame.mouse.get_pressed()
    if x + w > mouse[0] > x and y + h > mouse[1] > y:
        pygame.draw.rect(screen, active_color, (x, y, w, h))
        if click[0] == 1 and action is not None:
            action()
    else:
        pygame.draw.rect(screen, inactive_color, (x, y, w, h))

    small_text = pygame.font.Font("freesansbold.ttf", 20)
    text_surf = small_text.render(text, True, BLACK)
    text_rect = text_surf.get_rect()
    text_rect.center = ((x + (w / 2)), (y + (h / 2)))
    screen.blit(text_surf, text_rect)

def display_score(score, high_score):
    font = pygame.font.Font("freesansbold.ttf", 20)
    score_text = font.render(f"Score: {score}", True, WHITE)
    high_score_text = font.render(f"High Score: {high_score}", True, WHITE)
    screen.blit(score_text, (10, 10))
    screen.blit(high_score_text, (SCREEN_WIDTH - 150, 10))

def display_pause_message():
    font = pygame.font.Font("freesansbold.ttf", 50)
    pause_text = font.render("Paused", True, RED)
    text_rect = pause_text.get_rect(center=(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2))
    screen.blit(pause_text, text_rect)

def display_game_over():
    font = pygame.font.Font("freesansbold.ttf", 50)
    game_over_text = font.render("Game Over", True, RED)
    text_rect = game_over_text.get_rect(center=(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2))
    screen.blit(game_over_text, text_rect)

def start_screen():
    start = True
    while start:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()

        if background_image:
            screen.blit(background_image, (0, 0))
        else:
            screen.fill(BLACK)

        large_text = pygame.font.Font("freesansbold.ttf", 50)
        text_surf = large_text.render("Snake Game", True, WHITE)
        text_rect = text_surf.get_rect()
        text_rect.center = ((SCREEN_WIDTH / 2), (SCREEN_HEIGHT / 2 - 100))
        screen.blit(text_surf, text_rect)

        draw_button("Start", SCREEN_WIDTH / 2 - 50, SCREEN_HEIGHT / 2, 100, 50, GREEN, BLUE, main)

        pygame.display.update()
        clock.tick(15)

def main():
    snake = [(GRID_WIDTH // 2, GRID_HEIGHT // 2)]
    direction = random.choice([UP, DOWN, LEFT, RIGHT])
    food = (random.randint(0, GRID_WIDTH - 1), random.randint(0, GRID_HEIGHT - 1))
    
    score = 0
    high_score = 0
    paused = False
    game_over = False

    running = True
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
            elif event.type == pygame.KEYDOWN:
                if event.key == pygame.K_UP and direction != DOWN:
                    direction = UP
                elif event.key == pygame.K_DOWN and direction != UP:
                    direction = DOWN
                elif event.key == pygame.K_LEFT and direction != RIGHT:
                    direction = LEFT
                elif event.key == pygame.K_RIGHT and direction != LEFT:
                    direction = RIGHT
                elif event.key == pygame.K_SPACE:
                    if game_over:
                        game_over = False
                        snake = [(GRID_WIDTH // 2, GRID_HEIGHT // 2)]
                        direction = random.choice([UP, DOWN, LEFT, RIGHT])
                        food = (random.randint(0, GRID_WIDTH - 1), random.randint(0, GRID_HEIGHT - 1))
                        score = 0

                    paused = not paused

        if not paused and not game_over:
            # Move the snake
            head_x, head_y = snake[0]
            new_head = (head_x + direction[0], head_y + direction[1])


            # Check for collisions
            if new_head in snake or new_head[0] < 0 or new_head[0] >= GRID_WIDTH or new_head[1] < 0 or new_head[1] >= GRID_HEIGHT:
                running = False  # Snake has collided with itself or the boundaries
            else:
                snake.insert(0, new_head)
                if new_head == food:
                    food = (random.randint(0, GRID_WIDTH - 1), random.randint(0, GRID_HEIGHT - 1))
                    score += 10
                    if score > high_score:
                        high_score = score
                else:
                    snake.pop()

        # Render everything
        screen.fill(BLACK)
        if background_image:
            screen.blit(background_image, (0, 0))
        draw_grid()
        draw_snake(snake)
        draw_food(food)
        display_score(score, high_score)
        if paused:
            display_pause_message()
        pygame.display.flip()
        clock.tick(FPS)

    pygame.quit()
    sys.exit()

if __name__ == "__main__":
    start_screen()
