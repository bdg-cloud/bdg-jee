CREATE TABLE Users(username VARCHAR(64) PRIMARY KEY, passwd VARCHAR(64));
CREATE TABLE UserRoles(username VARCHAR(64), userRoles VARCHAR(32));

nicolas:lgr006
legrain:lgr
insert into Users values('nicolas','VLICnfhhmeKqsofW4g8gD18GuPR1U6PBjf/1BgoeYW4=');
insert into Users values('legrain','CPb1ngVAlEYiyUF2zQ0SnyKLNRvBQWnBB1MLc+FcC70=');
insert into UserRoles values('nicolas','admin');
insert into UserRoles values('legrain','utilisateur');
  