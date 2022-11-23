
    create table Administrador (
        id int8 not null,
        apellido varchar(255),
        contrasena varchar(255),
        nombre varchar(255),
        nombreUsuario varchar(255),
        primary key (id)
    )

    create table DatosActividad (
        id int8 not null,
        periodicidadDeImputacion date,
        tipoPeriodicidad int4,
        valor float4 not null,
        tipoConsumo_id int8,
        primary key (id)
    )

    create table Linea (
        id int8 not null,
        primary key (id)
    )

    create table Linea_Parada (
        Linea_id int8 not null,
        paradasVuelta_id int8 not null,
        paradasIda_id int8 not null
    )

    create table MedioDeTransporte (
        DTYPE varchar(31) not null,
        id int8 not null,
        consumoDeCombustiblePorKm float4,
        esPrivado boolean,
        tipo_vehiculo int4,
        tipo_transporte_publico int4,
        tipoConsumo_id int8,
        lineaDeTransporte_id int8,
        primary key (id)
    )

    create table Miembro (
        id int8 not null,
        apellido varchar(255),
        documento int4,
        nombre varchar(255),
        tipo int4,
        primary key (id)
    )

    create table Miembro_Trayecto (
        Miembro_id int8 not null,
        trayectos_id int8 not null
    )

    create table Organizacion (
        id int8 not null,
        clasificacion int4,
        razonSocial varchar(255),
        tipo int4,
        ubicacionGeografica_localidadId int4,
        primary key (id)
    )

    create table Organizacion_DatosActividad (
        Organizacion_id int8 not null,
        actividades_id int8 not null
    )

    create table Organizacion_Sector (
        Organizacion_id int8 not null,
        sectores_id int8 not null
    )

    create table Organizacion_contacto (
        Organizacion_id int8 not null,
        contactos_id int8 not null
    )

    create table Parada (
        id int8 not null,
        distanciaProxima float4 not null,
        nombre varchar(255),
        ubicacion_localidadId int4,
        primary key (id)
    )

    create table Sector (
        id int8 not null,
        primary key (id)
    )

    create table Sector_Miembro (
        Sector_id int8 not null,
        postulantes_id int8 not null,
        miembros_id int8 not null
    )

    create table TipoConsumo (
        id int8 not null,
        factorEmision float4 not null,
        tipoActividad varchar(255),
        tipoAlcance int4,
        tipoDescripcion varchar(255),
        tipoUnidad int4,
        primary key (id)
    )

    create table Tramo (
        id int8 not null,
        destino_localidadId int4,
        medioDeTransporte_id int8,
        origen_localidadId int4,
        primary key (id)
    )

    create table Trayecto (
        id int8 not null,
        primary key (id)
    )

    create table Trayecto_Tramo (
        Trayecto_id int8 not null,
        tramos_id int8 not null
    )

    create table Ubicacion (
        localidadId int4 not null,
        altura int4 not null,
        calle varchar(255),
        primary key (localidadId)
    )

    create table contacto (
        id int8 not null,
        mail varchar(255),
        nombre varchar(255),
        telefono varchar(255),
        primary key (id)
    )

    create table sector_territorial (
        id int8 not null,
        nombre varchar(255),
        tipo int4,
        primary key (id)
    )

    create table sector_territorial_Organizacion (
        sector_territorial_id int8 not null,
        organizaciones_id int8 not null
    )

    alter table Sector_Miembro 
        add constraint UK_7ghpx5yb4erwl25d7nmhp7b1r  unique (postulantes_id)

    alter table Sector_Miembro 
        add constraint UK_pb6wj21q906vka2wgjxdeerxk  unique (miembros_id)

    alter table DatosActividad 
        add constraint FK_nnxc9dmo64wp5osmyrlirgwlf 
        foreign key (tipoConsumo_id) 
        references TipoConsumo

    alter table Linea_Parada 
        add constraint FK_dgvrxsw6akvywkpfjosibi61o 
        foreign key (paradasVuelta_id) 
        references Parada

    alter table Linea_Parada 
        add constraint FK_hw8xb3tgch4ijo13393h4j1k3 
        foreign key (Linea_id) 
        references Linea

    alter table Linea_Parada 
        add constraint FK_b5x3dgpviabx9951sbq5x4c4i 
        foreign key (paradasIda_id) 
        references Parada

    alter table MedioDeTransporte
        add constraint FK_ike1e0tq79xfbysxuoghb8yt6 
        foreign key (tipoConsumo_id) 
        references TipoConsumo

    alter table MedioDeTransporte
        add constraint FK_odiilgdq7o5ilnkvr576moyl0 
        foreign key (lineaDeTransporte_id) 
        references Linea

    alter table Miembro_Trayecto 
        add constraint FK_xhrxvnxm03l0snangdqucrg4 
        foreign key (trayectos_id) 
        references Trayecto

    alter table Miembro_Trayecto 
        add constraint FK_t131kww33qy437i4ok3fh5i2y 
        foreign key (Miembro_id) 
        references Miembro

    alter table Organizacion 
        add constraint FK_nc14in1cax8qvg2sydqidd37 
        foreign key (ubicacionGeografica_localidadId) 
        references Ubicacion

    alter table Organizacion_DatosActividad 
        add constraint FK_s5i8e9vg3m8wa81pj8xqa4d0f 
        foreign key (actividades_id) 
        references DatosActividad

    alter table Organizacion_DatosActividad 
        add constraint FK_npiqjsgesu12ptljyis9yejox 
        foreign key (Organizacion_id) 
        references Organizacion

    alter table Organizacion_Sector 
        add constraint FK_nw0rj4m3gef0us0yow6a12p8a 
        foreign key (sectores_id) 
        references Sector

    alter table Organizacion_Sector 
        add constraint FK_p2ooegjkwau2sm9len55fnxsg 
        foreign key (Organizacion_id) 
        references Organizacion

    alter table Organizacion_contacto 
        add constraint FK_tg5nrctebnsbi55qv5y8tbfw 
        foreign key (contactos_id) 
        references contacto

    alter table Organizacion_contacto 
        add constraint FK_r813erxkpd0cy5m97leqq6vit 
        foreign key (Organizacion_id) 
        references Organizacion

    alter table Parada 
        add constraint FK_4jbx8rc310rn6fruw7giscki2 
        foreign key (ubicacion_localidadId) 
        references Ubicacion

    alter table Sector_Miembro 
        add constraint FK_7ghpx5yb4erwl25d7nmhp7b1r 
        foreign key (postulantes_id) 
        references Miembro

    alter table Sector_Miembro 
        add constraint FK_3rnyllwgufvkqj7iyg1hrd7dt 
        foreign key (Sector_id) 
        references Sector

    alter table Sector_Miembro 
        add constraint FK_pb6wj21q906vka2wgjxdeerxk 
        foreign key (miembros_id) 
        references Miembro

    alter table Tramo 
        add constraint FK_2axg4wa5de58yfvr092miuy0r 
        foreign key (destino_localidadId) 
        references Ubicacion

    alter table Tramo 
        add constraint FK_hvj6aq4ybsu2d3dbrpj7khbme 
        foreign key (medioDeTransporte_id) 
        references MedioTransportev2

    alter table Tramo 
        add constraint FK_hre73xsr1o24urq2ayq5wxctb 
        foreign key (origen_localidadId) 
        references Ubicacion

    alter table Trayecto_Tramo 
        add constraint FK_pf6xn10uc24t3g928rpqvx7wc 
        foreign key (tramos_id) 
        references Tramo

    alter table Trayecto_Tramo 
        add constraint FK_7or3fijwiu4bm0e2chwaa3f2e 
        foreign key (Trayecto_id) 
        references Trayecto

    alter table sector_territorial_Organizacion 
        add constraint FK_46jrb0vjyq4885lf0rprwkxro 
        foreign key (organizaciones_id) 
        references Organizacion

    alter table sector_territorial_Organizacion 
        add constraint FK_7ajfoll8u4mu647mdlmkbnp1m 
        foreign key (sector_territorial_id) 
        references sector_territorial

    create sequence hibernate_sequence
