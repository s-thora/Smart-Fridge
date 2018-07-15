---- functions returning random data

create or replace function random_grocery() returns varchar language sql as
$$
select grocery_name from "Groceries" order by random() limit 1
$$;

create or replace function random_unit() returns varchar language sql as
$$
select unit_name from unit_names order by random() limit 1
$$;

create or replace function random_dish() returns varchar language sql as
$$
select dish_name from dish_names order by random() limit 1
$$;

create or replace function random_recipe_content() returns varchar language sql as
$$
select recipe_text from recipe_texts order by random() limit 1
$$;

create or replace function random_user() returns varchar language sql as
$$
select user_name from user_names order by random() limit 1
$$;

create or replace function random_grocery_id() returns integer language sql as
$$
select id from "Groceries" order by random() limit 1
$$;

create or replace function random_dish_id() returns integer language sql as
$$
select id from "Menu" order by random() limit 1
$$;

drop function if exists random_sub_groceries();
create or replace function random_sub_groceries(n numeric)
returns table (id integer, grocery_name varchar, price numeric, minimal_amount numeric, keeping_time int, category_id int)
language sql as
$$
select id, grocery_name, price, minimal_amount, keeping_time, category_id
from "Groceries" order by random() limit cast((select count(*) from "Groceries") * n as int)
$$;

drop function if exists random_sub_inventory();
create or replace function random_sub_inventory(n numeric)
returns table (id int, grocery_id int, production_date date, added timestamp, removed timestamp, amount numeric)
language sql as
$$
select id, grocery_id, production_date, added, removed, amount
from "Inventory" order by random() limit cast((select count(*) from "Inventory") * n as int)
$$;

---- functions getting data

drop function if exists get_spoiled_items();
create or replace function get_spoiled_items()
returns table(grocery_id int, grocery_name varchar, amount numeric,
			  minimal_amount numeric, added timestamp, keeping_time int)
language sql as
$$
select	i.grocery_id,
		g.grocery_name,
		i.amount,
		g.minimal_amount,
		i.added,
		g.keeping_time
from "Groceries" as g right join "Inventory" as i on g.id=i.grocery_id
where (i.production_date + g.keeping_time < now())
limit 25
$$;
