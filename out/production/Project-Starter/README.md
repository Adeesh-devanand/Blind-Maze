# Blind Maze

## Project Overview

The application has **two** basic windows. A **menu window**, where a user will be able to **choose** from a 
selection of pre-made mazes, or **create** a new maze of his own. When a maze is selected, a **play/edit** window will
open up, where he/she will be able to make changes to the maze if he is in edit mode, or try solving the maze if he 
is in play mode. While in editing mode, he/she will be able to **add** obstacles to the maze, or change the 
player/monster positions. 

The _project title_ is derived from the next part of the project, While in the player mode, they will start from
**any random (pre-selected) place** in the map, with the goal of **catching the monster**. 
_They will only be able to know the blocks within a preset block radius of the character_.
When they move their character, their vision moves with them. The game **gets over** when the user touches the monster.

This game targets a community of computer users who feels like taking a **short break** from their work, or 
**seasoned gamers** who want to view maze games in a different light. It could be of use to someone who is looking for 
games which let them be the game master, with the whole **"creation"** aspect of the game.

For the past year I have been interested in **competitive coding**, and have been experimenting with various 
**graphing algorithms**. I believe this project is quite challenging, and meanwhile still doable. I also feel that
coding the "monster" in the game provides me with the perfect opportunity to further experiment with graphing 
algorithms. 

## User Stories

- As a user in the menu, I want to be able to add new mazes to my Game
- As a user in the menu, I want to be able to select one maze to work with
- As a user in the menu, I want to be able to toggle between edit and play mode
- As a user opening a maze in edit mode, I want to be able to place entities on my maze
- As a user opening a maze in play mode, I want to be able to move my player around
- As a user opening a maze in any mode, I want to be able to quit the maze that is currently open
- As a user, I want to be able to exit the application from the menu
- As a user, I want the option to save while quiting the application
- As a user, I want the option to load my previous progress when I run the application

## Phase 4: Task 2

Sun Apr 09 18:23:05 PDT 2023|  Created maze: "MyFirstMaze"

Sun Apr 09 18:23:12 PDT 2023|  Opened maze: "MyFirstMaze"

Sun Apr 09 18:23:13 PDT 2023|  cursor moved: r

Sun Apr 09 18:23:13 PDT 2023|  Placed obstacle at: 1 0

Sun Apr 09 18:23:15 PDT 2023|  Quit current maze

Sun Apr 09 18:23:18 PDT 2023|  Toggled to: PlayMode

Sun Apr 09 18:23:20 PDT 2023|  Opened maze: "MyFirstMaze"

Sun Apr 09 18:23:23 PDT 2023|  Player moved: d

Sun Apr 09 18:23:25 PDT 2023|  Quit current maze

Saved game to ./data/workspace.json

## Phase 4: Task 3

Right now the **Position** class is present in the model package as there are substantial dependencies with the **Maze**
class. The maze class acts as an abstraction for grid, providing only the functionality needed by game. If I had more
time I would try to move the Position class into the grid package and use a layer of abstraction between Position and
Maze to reduce.

Another change would be to make appropriate changes to the classes in the grid package to get rid of the upward 
association between the **<<abstract>> MovableElement** and **Grid** classes. That way I could freely make changes in 
grid without having to propagate the changes to the **MovableElement** class.