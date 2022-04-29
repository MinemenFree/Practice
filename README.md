# cPractice

Highly configurable practice plugin. This project was originally closed source and is now open source.

<h1 align="center">How to Use?</h1>

1) Install like any other plugin by dragging it into your server's plugin folder
2) Start the server & let the plugin load config files
3) stop the server & Configure the plugin to your liking
4) Done!

<h2 align="center">Commands</h2>

<h4>**Setup Commands**</h4>

/kit - List kit commands
/kits (view kit list)

/arena - List arena commands
/arenas (view arena list)

/setspawn - set Lobby (permission: cpractice.command.setspawn)

<h4>Essential Commands</h4>

Player commands:
/lang - choose language | permission: not required
/ping - get player ping | permission: not required
/reset - reset player inventory & send to spawn | permission: cpractice.command.spawn
/spawn - tp to spawn | permission: cpractice.command.reset
/viewmatch - recent match history of a player in a gui | permission: cpractice.viewmatch

Donator Commands:
/fly - toggle fly mode | permission: cpractice.fly
/rename - change held item's name | permission: cpractice.command.rename
/showallplayers - reveal all players | permission: cpractice.command.showallplayers
/showplayer - reveal mentioned player | permission: cpractice.command.showplayer

Staff Commands:
/clear - clear inventory | permission: cpractice.command.clearinv
/day - set time to day | permission: cpractice.command.day
/night - set time to night | permission: cpractice.command.night
/sunset - set time to sunset | permission: cpractice.command.sunset
/gamemode - change gamemode | permission: cpractice.command.gamemode
/heal - heal player | permission: cpractice.command.heal
/location - get a player's location | permission: cpractice.command.loc
/more - get more of the held item | permission: v.command.more
/sudo - sudo a player | permission: cpractice.command.sudo
/sudoall - sudo every player | permission : cpractice.command.sudoall

Admin Commands:
/admin - show plugin info | permission: cpractice.owner
/cpractice - helpful subcommands | permission: cpractice.owner
/setslots - set max server slots | permission: cpractice.command.setslots
/world - tp to another world, useful for arena setup | permission: cpractice.command.tpworld
/troll - opens Demo menu to player xD | permission: cpractice.troll
![alt text](https://i.imgur.com/EiNqxzW.png)



