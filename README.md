# cPractice

Highly configurable practice plugin. This project was originally closed source and is now open source.

<h1 align="center">How to Use?</h1>

1) Install like any other plugin by dragging it into your server's plugin folder\
2) Start the server & let the plugin load config files\
3) stop the server & Configure the plugin to your liking\
4) Done!\
![alt text](https://i.imgur.com/uzRYQI7.png)

<h2 align="center">Commands</h2>

<h4>Setup Commands</h4>
    
/kit - List kit commands\
/kits (view kit list)\
![alt text](https://i.imgur.com/BdYGvNU.png)


/arena - List arena commands\
/arenas (view arena list)\
![alt text](https://i.imgur.com/L9Mym64.png)

/setspawn - set Lobby (permission: cpractice.command.setspawn)

<h4>Essential Commands</h4>

Player commands:\
    /lang - choose language | permission: not required\
    /ping - get player ping | permission: not required\
    /reset - reset player inventory & send to spawn | permission: cpractice.command.spawn\
    /spawn - tp to spawn | permission: cpractice.command.reset\
    /viewmatch - recent match history of a player in a gui | permission: cpractice.viewmatch\
\
Donator Commands:\
    /fly - toggle fly mode | permission: cpractice.fly\
    /rename - change held item's name | permission: cpractice.command.rename\
    /showallplayers - reveal all players | permission: cpractice.command.showallplayers\
    /showplayer - reveal mentioned player | permission: cpractice.command.showplayer\
\
Staff Commands:\
    /clear - clear inventory | permission: cpractice.command.clearinv \
    /day - set time to day | permission: cpractice.command.day\
    /night - set time to night | permission: cpractice.command.night\
    /sunset - set time to sunset | permission: cpractice.command.sunset\
    /gamemode - change gamemode | permission: cpractice.command.gamemode\
    /heal - heal player | permission: cpractice.command.heal\
    /location - get a player's location | permission: cpractice.command.loc\
    /more - get more of the held item | permission: v.command.more\
    /sudo - sudo a player | permission: cpractice.command.sudo\
    /sudoall - sudo every player | permission : cpractice.command.sudoall\
\
Admin Commands:\
    /admin - show plugin info | permission: cpractice.owner\
    ![alt text](https://i.imgur.com/rfaxQe8.png)
\
    /cpractice - helpful subcommands | permission: cpractice.owner\
    ![alt text](https://i.imgur.com/3qmYYI9.png)
\
    /setslots - set max server slots | permission: cpractice.command.setslots\
    /world - tp to another world, useful for arena setup | permission: cpractice.command.tpworld\
    /troll - opens Demo menu to player xD | permission: cpractice.troll\
    ![alt text](https://i.imgur.com/EiNqxzW.png)


<h3>Clan Help</h3>

![alt text](https://i.imgur.com/pEmx9Ti.png)



<h3>Ablities Help</h3>

![alt text](https://i.imgur.com/FTcrUsx.png)


<h3>Coins Manager</h3>

![alt text](https://i.imgur.com/rvrEaId.png)


<h3>Cosmetics Shop</h3>

<h4>Kill Effects Shop</h4>

![alt text](https://i.imgur.com/7ViLVhi.png)

<h4>Trails Effects Shop</h4>

![alt text](https://i.imgur.com/U7NfVtp.png)


<h1 align="center">Placeholders</h1>

<h2>Holographics Displays - GLOBAL LB</h3>

Name:   {globaltop(0-9)_name}\
ELO:    {globaltop(0-9)_elo}\
\
_eg: 1. {globaltop0_name} - {globaltop0_elo}_


<h2>Holographics Displays - Per Ladder LB</h3>

Name:   {top(0-9)_kit_name}\
ELO:    {top(0-9)_kit_elo}\
\
_eg: 1. {top0_nodebuff_name} - {top0_nodebuff_elo}_\


<h2>Holographics Displays - CLAN LB</h3>

Name:        {clantop(0-4)_name}\
Catergory:   {clantop(0-4)_category}\
Points:      {clantop(0-4)_elo}\
\
_eg: 1. {clantop0_category} || {clantop0_name} - {clantop(0-4)_elo}_\

