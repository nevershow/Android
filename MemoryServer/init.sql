create table if not exists Task
( uuid VARCHAR(40) NOT NULL PRIMARY KEY,
  topic TEXT NOT NULL,
  ddl TEXT NOT NULL,
  detail TEXT,
  isTimeRemind TEXT,
  isPlaceRemind TEXT,
  placeUid TEXT,
  placeDescription TEXT,
  longitude TEXT,
  latitude TEXT);
