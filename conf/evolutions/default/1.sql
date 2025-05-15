# --- !Ups

create table requests (
  id                            bigint auto_increment not null,
  method                        varchar(255),
  payload                       longtext,
  headers                       longtext,
  received_at                   bigint,
  constraint pk_requests primary key (id)
);

# --- !Downs

drop table if exists requests;

