insert into Movie (duration, title, id)
values (7200000000000, "Komedia romantyczna z Karolakiem", 1);
insert into Movie (duration, title, id)
values (4800000000000, "Ęśąćż", 2);
insert into Movie (duration, title, id)
values (3600000000000, "NullPointerException: the movie", 3);
insert into Movie (duration, title, id)
values (9600000000000, "Spring programming for dummies", 4);
insert into Movie (duration, title, id)
values (7200000000000, "Ferdydurke", 5);

insert into Room (numberOfRows, seatsPerRow, id)
values (5, 10, 1);
insert into Room (numberOfRows, seatsPerRow, id)
values (4, 4, 2);
insert into Room (numberOfRows, seatsPerRow, id)
values (7, 7, 3);


insert into Screening (movie_id, room_id, time, id)
values (1, 1, "2023-07-07 07:00:00", 1);
insert into Screening (movie_id, room_id, time, id)
values (3, 1, "2023-07-07 19:00:00", 2);
insert into Screening (movie_id, room_id, time, id)
values (4, 2, "2023-07-07 03:05:00", 3);
insert into Screening (movie_id, room_id, time, id)
values (2, 2, "2023-07-07 07:15:00", 4);
insert into Screening (movie_id, room_id, time, id)
values (3, 3, "2023-07-07 08:15:00", 5);
insert into Screening (movie_id, room_id, time, id)
values (5, 3, "2023-07-07 11:30:00", 6);