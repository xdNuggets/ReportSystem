# Report System for Spigot Minecraft Servers
## Currently supported versions: 1.8

# Features
- Ability to create reports as a player, with a custom reason
- Ability for staff members to view all active reports, edit them, input a reason for the edit.
- Ability for staff members to view details about a report using the ID
- Ability for administrators to change settings, messages, commands, in a config.




# TODO (Version 2)
- [x] Allow ability for multiple reports to be made on the same person
- [x] Allow admins to edit reports. This includes denying them, accepting them, and changing the reason.
- [ ] Allow ability to cancel reports made. (WIP)
- [ ] Create notification system when someone joins the server and their report has been edited (Accepted/Denied).
- [ ] Ability to view all individual reports made on a person (this ones gonna be painful)
- [ ] Discord Bot integration (Begun)
- [ ] Add Status to reports (Pending, Accepted, Denied)
- [ ] Create individual report check command (using IDs).

# Less important TODO
- [ ] Convert the entire plugin to Kotlin (don't know any)
- [ ] Possibly make this into a Bungee Plugin (not certain whether I want to do this)
- [x] Rework ID System
- [ ] Add extra info to report info (on click probably). This will include first played, last played, and maybe alts? (this has become a command)
- [ ] Statistics for both staff members and players

# Known bugs
- /rs settings doesn't update
- "prefix" variable in HoverText in ReportCommand.java is bugged (not sure if a fix is possible)
- No commands will work if a player is offline. Not sure how im gonna tackle this

