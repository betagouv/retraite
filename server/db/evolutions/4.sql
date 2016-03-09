# --- !Ups

create table ChapitreCondition (
    id bigint not null auto_increment,
    chapitre_id bigint,
    primary key (id)
);

create table ConditionProps (
    condition_id bigint not null,
    cle varchar(255) not null,
    valeur varchar(255),
    primary key (condition_id, cle)
);

alter table ChapitreCondition 
    add index FK_ddlf6p8ayygvfe0oir25yq4fs (chapitre_id), 
    add constraint FK_ddlf6p8ayygvfe0oir25yq4fs foreign key (chapitre_id) references Chapitre (id);

alter table ConditionProps 
    add index FK_eyl1kkmha8hv2t7tdguw7e65n (condition_id), 
    add constraint FK_eyl1kkmha8hv2t7tdguw7e65n foreign key (condition_id) references ChapitreCondition (id);

# --- !Downs

ALTER TABLE ConditionProps DROP foreign key FK_eyl1kkmha8hv2t7tdguw7e65n;
ALTER TABLE ConditionProps DROP INDEX FK_eyl1kkmha8hv2t7tdguw7e65n;

ALTER TABLE Condition DROP foreign key FK_ddlf6p8ayygvfe0oir25yq4fs;
ALTER TABLE Condition DROP INDEX FK_ddlf6p8ayygvfe0oir25yq4fs;

drop table ConditionProps;
drop table Condition;