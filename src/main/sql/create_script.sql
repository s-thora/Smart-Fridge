/*
 *inspired by shanki
 */

drop table if exists "Categories" cascade;
create table "Categories" (
    id serial primary key,
    category_name varchar,
    measurement_unit varchar
);

drop table if exists "Menu" cascade;
create table "Menu" (
    id serial primary key,
    dish_name varchar,
    price numeric,
	recipe_content varchar,
	cooking_time varchar
);

drop table if exists "Usage types" cascade;
create table "Usage types" (
    id serial primary key,
    type_name varchar
);

drop table if exists "Usage registration" cascade;
create table "Usage registration" (
    id serial primary key,
    usage_timestamp timestamp,
	user_name varchar
);

drop table if exists "Shopping lists" cascade;
create table "Shopping lists" (
	id serial primary key,
	created timestamp,
	insertion_id int references "Usage registration"
);

drop table if exists "Shopping list items" cascade;
create table "Shopping list items" (
	shopping_list_id int references "Shopping lists" on delete cascade,
	grocery_id int references "Groceries",
	amount numeric
);

drop table if exists "Orders" cascade;
create table "Orders" (
	id serial,
	shopping_list_id int references "Shopping lists" on delete set null,
	order_date timestamp,
    sum numeric
);

drop table if exists "Groceries" cascade;
create table "Groceries" (
    id serial primary key,
    grocery_name varchar,
    price numeric,
    minimal_amount numeric,
    keeping_time int, -- days
    category_id int references "Categories"
);

drop table if exists "Recipe groceries" cascade;
create table "Recipe groceries" (
    grocery_id int references "Groceries" on delete set null,
    dish_id int references "Menu" on delete set null,
	amount int
);

drop table if exists "Inventory" cascade;
create table "Inventory" (
	id serial primary key,
    grocery_id int references "Groceries" on delete set null,
	production_date date,
    added timestamp,
	removed timestamp,
    amount numeric
);

drop table if exists "Registered items" cascade;
create table "Registered items" (
    grocery_id int references "Groceries" on delete set null,
	inventory_id int references "Inventory",
    usage_id int, --references "Usage registration" later
    amount numeric,
    type_id int references "Usage types"
);