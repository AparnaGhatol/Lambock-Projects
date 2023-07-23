DROP TABLE IF EXISTS PRODUCT;

create table product (
        id bigint not null auto_increment,
        discount integer,
        name varchar(100) not null,
        price integer not null,
        sku varchar(12),
        stock_status varchar(25),
        primary key (id)
    );
  
alter table product 
       add constraint UK_jnameqr5b unique (name);