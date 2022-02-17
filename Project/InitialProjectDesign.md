# Project outlines/features:
- Implement app with medium level complexity that gives me experience in front-end and back-end development
- Front-end component starts with user registration screen for new users 
- Login screen for registered users, since discord has proprietary features, I may only end up developing a simplified version of a discord server that can communicate with android apps
- Once a user is authenticated, the UI displays all the channels the user is a part of (only one channel can be opened at a time). 
- When the user selects a channel to chat in, active users will be displayed on the side
- Messages sent into the channel can be sent publicly or privately
- Initially, only texts can be sent. No VoIP or real-time video feature will be implemented
- Messages are archived on database servers, but the Android database will have cached data to improve performance. The goal is to practice database programming on Android platforms: 
- Archived messages can be searched by keyword
- User can choose to edit or delete his own message
# Database Schema 	
```
@Entity
data class User(
  @PrimaryKey val uid: Int,
  @ColumnInfo(name = "first_name") val firstName: String?,
  @ColumnInfo(name = "last_name") val lastName: String?,
  @ColumnInfo(name = "email") val email: String?,
  @ColumnInfo(name = "encrypt_password") val encryptPassword: String?
   )
 @Entity
 data class Membership(
  @PrimaryKey val mid: int,
  @ColumnInfo(name = “uid”) val uid: int?,
  @Columninfo(name = “cid”) val cid: int?
 )

@Entity
data class Channel(
  @PrimaryKey val cid: Int,
  @ColumnInfo(name = "channel_name") val channelName: String?,
  @ColumnInfo(name = "last_name") val lastName: String?,
  @ColumnInfo(name = "email") val email: String?,
  @ColumnInfo(name = "encrypt_password") val encryptPassword: String?
  )

 @Entity
 data class Message(
  @PrimaryKey val msgid: int,
  @ColumnInfo(name = “from”) val from: int?,
  @Columninfo(name = “to”) val to: int?
  @Columninfo(name = “body”) val to: String?
  @Columninfo(name = “channel”) val channel: int?
   )

```

