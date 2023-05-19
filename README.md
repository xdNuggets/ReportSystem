# ReportSystem
My Spigot report system. Unfinished


# TODO (Version 2)
- [x] Allow ability for multiple reports to be made on the same person
- [x] Allow admins to edit reports. This includes denying them, accepting them, and changing the reason.
- [ ] Allow ability to cancel reports made. (WIP)
- [ ] Create notification system when someone joins the server and their report has been edited (Accepted/Denied).
- [ ] Discord Bot integration (Begun)

# Less important TODO
- [ ] Convert the entire plugin to Kotlin (don't know any)
- [ ] Possibly make this into a Bungee Plugin (not certain whether I want to do this)
- [x] Rework ID System
- [ ] Save reports when onDisable is ran
- [ ] Add extra info to report info (on click probably). This will include first played, last played, and maybe alts?
- [ ] Statistics for both staff members and players

# Known bugs
- /rs settings doesn't update
- "prefix" variable in HoverText in ReportCommand.java is bugged (not sure if a fix is possible)
- When accepting/denying a report, I found out another report is created with a different ID. Trying to find the cause atm

