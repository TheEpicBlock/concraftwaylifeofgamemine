# con-craft way life of game mine
Implements Conway's game of life in minecraft blocks!
## using
This mod works with special blocks called "conway life of game blocks". These blocks count as alive. All other blocks are considered dead.  
These Conway blocks can replace [almost](#notes) any block, and will drop the block they replace. Conway blocks can also be pushed with pistons.
### mechanics
Conway blocks update every 3 seconds. Would've added a config for that. But didn't have the time, so you're stuck with 3 seconds. Each Y layer is considered seperately.
### obtaining
Conway blocks can either be crafted with an eye of ender surrounded with grass.  
They can also be mined with a silk touch pickaxe
### what can I do with this?
Use your imagination :P
For some ideas, you could make a parkour, maybe even giving the player some conway blocks to strategically place.  
You could also use this to farm blocks, since they drop blocks they break. Maybe even make some moving machines.
## performance
Con-craft way life of game mine is surprisingly quite performant. When no blocks are present, you shouldn't notice any lag. With a lot of blocks lag will happen though. This is largely due to the amount of blocks changed, something I can't optimize. Con-craft way life of game mine adds a section to the profiler to track the amount of time it takes to calculate and replace blocks.
### chunks
Conway blocks will kinda load chunks when needed. The loaded chunk won't tick, redstone won't function etc. The only thing that'll happen in that chunk are conway block updates.
## notes
[1] The `wither_immune` block tag is being used to check which blocks can be broken or not