drop table if exists detalle_pedido;
drop table if exists cabecera_pedidos;
drop table if exists estados_pedidos;
drop table if exists proveedores;
drop table if exists tipo_documento;
drop table if exists historial_stock;
drop table if exists detalle_ventas;
drop table if exists cabecera_ventas;
drop table if exists producto;
drop table if exists udm;
drop table if exists categoria_unidades_medidas;
drop table if exists categorias;
-- TABLA CATEGORIA 
create table categorias(
	codigo_cat int not null,
	nombre varchar(100) not null,
	categoria_padre int,
	constraint categorias_pk primary key (codigo_cat),
	constraint categorias_fk foreign key (categoria_padre) references categorias(codigo_cat) 
);
insert into categorias(codigo_cat,nombre, categoria_padre)
values(1,'materia prima',null);
insert into categorias(codigo_cat,nombre, categoria_padre)
values(2,'protenia',1);
insert into categorias(codigo_cat,nombre, categoria_padre)
values(3,'salsas',1);
insert into categorias(codigo_cat,nombre, categoria_padre)
values(4,'punto de venta',null);
insert into categorias(codigo_cat,nombre, categoria_padre)
values(5,'bebidas',4);
insert into categorias(codigo_cat,nombre, categoria_padre)
values(6,'con alcohol',5);
insert into categorias(codigo_cat,nombre, categoria_padre)
values(7,'sin alcohol',5);

select* from categorias;

-- TABLA CATEGORIA UNIDADES MEDIDAS
create table categoria_unidades_medidas(
	codigo_cat_udm char(1) not null,
	nombre varchar(100) not null,
	constraint categoria_unidades_medidas_pk primary key (codigo_cat_udm)
);
insert into categoria_unidades_medidas(codigo_cat_udm,nombre)
values('U','unidades');
insert into categoria_unidades_medidas(codigo_cat_udm,nombre)
values('V','volumen');
insert into categoria_unidades_medidas(codigo_cat_udm,nombre)
values('P','peso');

select*from categoria_unidades_medidas;

-- TABLA UNIDADES DE MEDIDAS
create table udm(
	codigo_udm char(1) not null,
	nombre varchar(20) not null,
	descripcion varchar(100) not null,
	categoria_udm char(1) not null,
	constraint unidades_medidas primary key (codigo_udm),
	constraint categoria_unidades_medidas_udm_fk foreign key (categoria_udm) references categoria_unidades_medidas(codigo_cat_udm) 
);
insert into udm(codigo_udm,nombre,descripcion,categoria_udm)
values('1','ml','mililitros','V');
insert into udm(codigo_udm,nombre,descripcion,categoria_udm)
values('2','l','litros','V');
insert into udm(codigo_udm,nombre,descripcion,categoria_udm)
values('3','u','unidad','U');
insert into udm(codigo_udm,nombre,descripcion,categoria_udm)
values('4','d','docena','U');
insert into udm(codigo_udm,nombre,descripcion,categoria_udm)
values('5','g','gramos','P');
insert into udm(codigo_udm,nombre,descripcion,categoria_udm)
values('6','kg','kilogramos','P');
insert into udm(codigo_udm,nombre,descripcion,categoria_udm)
values('7','lb','libras','P');

select *from udm;

--TABLA PRODUCTOS
create table producto(
	codigo_producto int not null,
	nombre varchar(100) not null,
	unidades_medidas char(1) not null,
	precio money,
	iva boolean,
	coste money,
	categoria int not null,
	stock int not null,
	constraint producto_pk primary key (codigo_producto),
	constraint producto_udm_fk foreign key (unidades_medidas) references udm(codigo_udm),
	constraint producto_categorias_fk foreign key (categoria) references categorias(codigo_cat)
);
insert into producto(codigo_producto,nombre,unidades_medidas,precio,iva,coste,categoria,stock)
values(1,'Coca cola pequeño','3',0.58,true,0.3729,2,100);
insert into producto(codigo_producto,nombre,unidades_medidas,precio,iva,coste,categoria,stock)
values(2,'Salsa de tomate','6',0.95,true,0.873,3,10);
insert into producto(codigo_producto,nombre,unidades_medidas,precio,iva,coste,categoria,stock)
values(3,'Mostaza','6',0.95,true,0.89,3,0);
insert into producto(codigo_producto,nombre,unidades_medidas,precio,iva,coste,categoria,stock)
values(4,'Fuztea','3',0.80,true,0.72,2,50);

select*from producto;

--TABLA CABECERA VENTAS
create table cabecera_ventas(
	codigo_cv int not null,
	fecha timestamp,
	total_sin_iva money,
	iva money,
	total money,
	constraint cabecera_ventas_pk primary key (codigo_cv)
);

insert into cabecera_ventas(codigo_cv,fecha,total_sin_iva,iva,total)
values(1,'20/11/2023 20:00',3.26,0.39,3.65);
select*from cabecera_ventas;

--TABLA DETALLE VENTA 
create table detalle_ventas(
	codigo serial not null,
	cabecera_ventas int not null,
	producto int not null,
	cantidad int,
	precio_venta money,
	subtotal money,
	subtotal_iva money,
	constraint detalle_ventas_pk primary key (codigo),
	constraint cabecera_ventas_detalle_ventas_fk foreign key (cabecera_ventas) references cabecera_ventas(codigo_cv),
	constraint producto_detalle_ventas_fk foreign key (producto) references producto(codigo_producto)
);

insert into detalle_ventas(cabecera_ventas,producto,cantidad,precio_venta,subtotal,subtotal_iva)
values(1,1,5,0.58,2.90,3.25);
insert into detalle_ventas(cabecera_ventas,producto,cantidad,precio_venta,subtotal,subtotal_iva)
values(1,4,1,0.36,0.36,0.40);

select*from detalle_ventas;

--TABLA HISTORIAL STOCK
create table historial_stock(
	codigo int not null,
	fecha timestamp,
	referencia varchar(100),
	producto int not null,
	cantidad int not null,
	constraint historial_stock_pk primary key (codigo),
	constraint historial_stock_producto_fk foreign key (producto) references producto(codigo_producto)
);

insert into historial_stock(codigo,fecha,referencia,producto,cantidad)
values(1,'20/11/2023 19:59','pedido 1',1,100);
insert into historial_stock(codigo,fecha,referencia,producto,cantidad)
values(2,'20/10/2023 19:59','pedido 4',1,50);
insert into historial_stock(codigo,fecha,referencia,producto,cantidad)
values(3,'21/11/2023 19:40','pedido 1',1,10);
insert into historial_stock(codigo,fecha,referencia,producto,cantidad)
values(4,'20/11/2023 20:59','venta 1',1,-5);
insert into historial_stock(codigo,fecha,referencia,producto,cantidad)
values(5,'20/11/2023 20:40','venta 1',4,1);

select * from historial_stock;

-- TABLA TIPO DE DOCUMENTO
create table tipo_documento(
	codigo char(1) not null,
	descripcion varchar(100) not null,
	constraint tipo_documento_pk primary key (codigo)
);

insert into tipo_documento(codigo,descripcion)
values('C', 'cedula');
insert into tipo_documento(codigo,descripcion)
values('R', 'ruc');

select*from tipo_documento;

--TABLA PROVEEDORES 
create table proveedores(
	indentificador char(20) not null,
	tipo_documento char(1) not null,
	nombre varchar(100),
	telefono varchar(20),
	correo varchar(50),
	direccion varchar(100),
	constraint proveedores_pk primary key (indentificador),
	constraint proveedores_tipo_documento_fk foreign key (tipo_documento) references tipo_documento(codigo)
);

insert into proveedores(indentificador, tipo_documento,nombre,telefono,correo,direccion)
values('1792285747','C','Santiago Mosquera','0992920306','Znty89@outlook.com','cumbaya');
insert into proveedores(indentificador, tipo_documento,nombre,telefono,correo,direccion)
values('1792285747001','R','Sancks SA','0992920306','sanck@outlook.com','la tola');

select * from proveedores;

--TABLA ESTADO PEDIDOS
create table estados_pedidos(
	codigo char(1) not null,
	descripcion varchar(100),
	constraint estados_pedidos_pk primary key (codigo)
);

insert into estados_pedidos(codigo,descripcion)
values('S','solicitado');
insert into estados_pedidos(codigo,descripcion)
values('R','recibido');

select * from estados_pedidos;

--TABLA CABECERA PEDIDO
create table cabecera_pedidos(
	numero int not null,
	proveedor varchar(20),
	fecha timestamp,
	estado char(1) not null,
	constraint cabecera_pedidos_pk primary key (numero),
	constraint proveedores_fk foreign key (proveedor) references proveedores(indentificador),
	constraint estado_pedidos_fk foreign key (estado) references estados_pedidos(codigo)
);

insert into cabecera_pedidos(numero,proveedor,fecha,estado)
values(1,'1792285747','20/11/2023','R');
insert into cabecera_pedidos(numero,proveedor,fecha,estado)
values(2,'1792285747','20/11/2023','R');

select * from cabecera_pedidos;

-- TABLA DETALLE PEDIDO
create table detalle_pedido(
	codigo serial not null,
	cabecera_pedido int not null,
	producto int not null,
	cantidad_solicitada int,
	subtotal money,
	cantidad_recibida int,
	constraint detalle_pedido_pk primary key (codigo),
	constraint cabecera_pedido_fk foreign key (cabecera_pedido) references cabecera_pedidos(numero),
	constraint producto_fk foreign key (producto) references producto(codigo_producto)
);

insert into detalle_pedido(cabecera_pedido,producto,cantidad_solicitada,subtotal,cantidad_recibida)
values(1,1,100,37.29,100);
insert into detalle_pedido(cabecera_pedido,producto,cantidad_solicitada,subtotal,cantidad_recibida)
values(1,4,50,11.80,50);
insert into detalle_pedido(cabecera_pedido,producto,cantidad_solicitada,subtotal,cantidad_recibida)
values(2,1,10,3.73,10);

select*from detalle_pedido;
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------
select indentificador, tipo_documento,nombre,telefono,correo,direccion from proveedores 
where upper(nombre) like '%SA%'