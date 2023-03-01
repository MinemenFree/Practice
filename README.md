## cPractice

cPractice is a flexible, easy to use and feature-rich practice plugin.

<h3 align="left">How to use?</h3>

1) Install like any other plugin by dragging it into your server's plugin folder
2) Start the server & let the plugin load config files
3) Configure the plugin to your liking & execute the following command:
 - /cpractice reload
4) Done!\
![alt text](https://i.imgur.com/uzRYQI7.png)

<h3 align="left">Commands</h3>

<h3>Setup Commands</h3>
    
/kit - List kit commands\
/kits (view kit list)\
![alt text](https://i.imgur.com/BdYGvNU.png)


/arena - List arena commands\
/arenas (view arena list)\
![alt text](https://i.imgur.com/L9Mym64.png)

/setspawn - set Lobby (permission: cpractice.command.setspawn)

<h3>Essential Commands</h3>

Player commands:\
    /lang - choose language | permission: not required\
    /reset - reset player inventory & send to spawn | permission: cpractice.command.spawn\
    /spawn - tp to spawn | permission: cpractice.command.reset\
    /viewmatch - recent match history of a player in a gui | permission: cpractice.viewmatch\
\
Donator Commands:\
    /rename - change held item's name | permission: cpractice.command.rename\
    /showallplayers - reveal all players | permission: cpractice.command.showallplayers\
    /showplayer - reveal mentioned player | permission: cpractice.command.showplayer\
\
Staff Commands:\
    /clear - clear inventory | permission: cpractice.command.clearinv \
    /day - set time to day | permission: cpractice.command.day\
    /night - set time to night | permission: cpractice.command.night\
    /sunset - set time to sunset | permission: cpractice.command.sunset\
    /location - get a player's location | permission: cpractice.command.loc\
    /more - get more of the held item | permission: v.command.more\
\
Admin Commands:\
    /cpracticeinfo - show plugin info | permission: cpractice.info\
    ![alt text](https://i.imgur.com/rfaxQe8.png)
\
    /cpractice - Helpful subcommands | permission: cpractice.admin\
    ![alt text](https://i.imgur.com/3qmYYI9.png)
\
    /setslots - Sets max server slots | permission: cpractice.command.setslots\
    /world - TP to another world, useful for arena setup | permission: cpractice.command.tpworld\
    /troll - Opens Demo menu to a player | permission: cpractice.troll\
    ![alt text](https://i.imgur.com/EiNqxzW.png)


<h3>Clan Help</h3>

![alt text](https://i.imgur.com/pEmx9Ti.png)



<h3>Ablities Help</h3>

![alt text](https://i.imgur.com/FTcrUsx.png)


<h3>Coins Manager</h3>

![alt text](https://i.imgur.com/rvrEaId.png)


<h3>Cosmetics Shop</h3>

<h3>Kill Effects Shop</h3>

![alt text](https://i.imgur.com/7ViLVhi.png)

<h3>Trails Effects Shop</h3>

![alt text](https://i.imgur.com/U7NfVtp.png)


<h3 align="left">Placeholders</h3>

<h3 align="left">Holographics Displays - GLOBAL LB</h3>

Name:   <globaltop(0-9)_name}\
ELO:    <globaltop(0-9)_elo}\
\
_eg: 1. <globaltop0_name} - <globaltop0_elo}_


<h3>Holographics Displays - Per Ladder LB</h3>

Name:   {top(0-9)_kit_name}\
ELO:    {top(0-9)_kit_elo}\
\
_eg: 1. {top0_nodebuff_name} - {top0_nodebuff_elo}_\


<h3>Holographics Displays - CLAN LB</h3>

Name:        <clantop(0-4)_name}\
Catergory:   <clantop(0-4)_category}\
Points:      <clantop(0-4)_elo}\
\
_eg: 1. <clantop0_category} || <clantop0_name} - <clantop(0-4)_elo}_\

