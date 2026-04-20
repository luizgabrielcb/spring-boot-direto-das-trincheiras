insert into "User" ("id", "email","firstName","lastName") values (1,'yusuke@yuyuhakusho.com','Yusuke','Urameshi');
insert into "User" ("id", "email","firstName","lastName") values (2,'hiei@yuyuhakusho.com','Hiei','Dragon');
insert into "Profile" ("id", "description", "name") values (1, 'Manages everything', 'Admin');
insert into "UserProfile" ("id", "user_id", "profile_id") values (1,1,1);
insert into "UserProfile" ("id", "user_id", "profile_id") values (2,2,1);