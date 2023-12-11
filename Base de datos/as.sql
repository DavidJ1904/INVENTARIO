select prod.codigo_producto, prod.nombre as nombre_producto, 
um.nombre as nombre_udm, um.descripcion as descripcion_udm,
cast (prod.precio as decimal(6,2)), prod.iva, 
cast (prod.coste as decimal (5,4)),
prod.categoria, cat.nombre as nombre_categoria,
stock 
from producto prod,udm um, categorias cat
where prod.unidades_medidas= um.codigo_udm
and prod.categoria = cat.codigo_cat
and upper(prod.nombre) like '%M%'


select * from producto prod,udm um, categorias cat
where prod.unidades_medidas= um.codigo_udm
and prod.categoria = cat.codigo_cat

insert into producto (codigo_producto,nombre,unidades_medidas,precio,iva,coste,categoria,stock)
values(5,'Mayoneza',3,0.98,'true',0.70,2,0)

select * from udm
select * from categorias

select * from cabecera_pedidos
select * from detalle_pedido

select * from historial_stock


update detalle_pedido
set cantidad_recibida =40, subtotal=20
where codigo=5
