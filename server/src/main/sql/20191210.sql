create database fullscalastack;

use fullscalastack;

CREATE TABLE `SampleModelObject` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` text NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into SampleModelObject (name) values ('One');
insert into SampleModelObject (name) values ('Two');
insert into SampleModelObject (name) values ('Three');
insert into SampleModelObject (name) values ('Four');
