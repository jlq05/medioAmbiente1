
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

    create table contactos (
        id int8 not null,
        mail varchar(255),
        nombre varchar(255),
        telefono varchar(255),
        primary key (id)
    )

    create table datosActividades (
        id int8 not null,
        periodicidadDeImputacion date,
        tipoPeriodicidad int4,
        valor float4 not null,
        tipoConsumo_id int8,
        organizacionId int8,
        primary key (id)
    )

    create table lineas (
        id int8 not null,
        primary key (id)
    )

    create table miembros (
        id int8 not null,
        persona_id int8,
        primary key (id)
    )

    create table miembros_trayectos (
        miembros_id int8 not null,
        trayectos_id int8 not null
    )

    create table organizaciones (
        id int8 not null,
        clasificacion int4,
        razonSocial varchar(255),
        tipo int4,
        altura int4 not null,
        calle varchar(255),
        localidadId int4 not null,
        sectorTerritorialId int8,
        primary key (id)
    )

    create table organizaciones_contactos (
        organizaciones_id int8 not null,
        contactos_id int8 not null
    )

    create table paradas (
        id int8 not null,
        distanciaProxima float4 not null,
        nombre varchar(255),
        altura int4 not null,
        calle varchar(255),
        localidadId int4 not null,
        lineaVueltaId int8,
        paradasVuelta_ORDER int4,
        lineaIdaId int8,
        paradasIda_ORDER int4,
        primary key (id)
    )

    create table personas (
        id int8 not null,
        apellido varchar(255),
        contrasenia varchar(255),
        documento int4,
        nombre varchar(255),
        nombreUsuario varchar(255),
        rol int4,
        tipo int4,
        primary key (id)
    )

    create table sectores (
        id int8 not null,
        nombre varchar(255),
        organizacionId int8,
        primary key (id)
    )

    create table sectoresTerritoriales (
        id int8 not null,
        nombre varchar(255),
        tipo int4,
        primary key (id)
    )

    create table sectores_miembros (
        sectores_id int8 not null,
        miembros_id int8 not null
    )

    create table sectores_postulantes (
        sectores_id int8 not null,
        postulantes_id int8 not null
    )

    create table tiposDeConsumo (
        id int8 not null,
        factorEmision float4 not null,
        tipoActividad varchar(255),
        tipoAlcance int4,
        tipoDescripcion varchar(255),
        tipoUnidad int4,
        primary key (id)
    )

    create table tramos (
        id int8 not null,
        destino_altura int4,
        destino_calle varchar(255),
        destino_localidad_id int4,
        origen_altura int4,
        origen_calle varchar(255),
        origen_localidad_id int4,
        medioDeTransporte_id int8,
        trayectoId int8,
        primary key (id)
    )

    create table trayectos (
        id int8 not null,
        periodicidadDeImputacion bytea,
        primary key (id)
    )

    alter table MedioDeTransporte 
        add constraint FK_jfoowo9opu40qhkrqnorb0eh0 
        foreign key (tipoConsumo_id) 
        references tiposDeConsumo

    alter table MedioDeTransporte 
        add constraint FK_67i7tups41iif553c3sy1csc2 
        foreign key (lineaDeTransporte_id) 
        references lineas

    alter table datosActividades 
        add constraint FK_n4us9knbogvq0h7rb1iutl8ge 
        foreign key (tipoConsumo_id) 
        references tiposDeConsumo

    alter table datosActividades 
        add constraint FK_mwoii94ai7ixdadgl5d99kp8b 
        foreign key (organizacionId) 
        references organizaciones

    alter table miembros 
        add constraint FK_c24c1evh6cmvy6s4gqdq8glwp 
        foreign key (persona_id) 
        references personas

    alter table miembros_trayectos 
        add constraint FK_9yja8hmju2cfpc102snsydkd 
        foreign key (trayectos_id) 
        references trayectos

    alter table miembros_trayectos 
        add constraint FK_64yvmy0ce9e0sgec2ug3u1noi 
        foreign key (miembros_id) 
        references miembros

    alter table organizaciones 
        add constraint FK_hhwisk56s00wo8sli8atk8oyo 
        foreign key (sectorTerritorialId) 
        references sectoresTerritoriales

    alter table organizaciones_contactos 
        add constraint FK_t1p4dm5k87sgreyo0mwj6bxom 
        foreign key (contactos_id) 
        references contactos

    alter table organizaciones_contactos 
        add constraint FK_od8jmoat52tt6iv50exom1k62 
        foreign key (organizaciones_id) 
        references organizaciones

    alter table paradas 
        add constraint FK_7q6bxl83nq67d2f2c588dhw18 
        foreign key (lineaVueltaId) 
        references lineas

    alter table paradas 
        add constraint FK_3w8okpsqtbntelixbq579hqsv 
        foreign key (lineaIdaId) 
        references lineas

    alter table sectores 
        add constraint FK_ih3rq5f6uhrl3qm1tw2islddn 
        foreign key (organizacionId) 
        references organizaciones

    alter table sectores_miembros 
        add constraint FK_f4a6l7o4s80ajjfrqdpm0okqn 
        foreign key (miembros_id) 
        references miembros

    alter table sectores_miembros 
        add constraint FK_j637oqje0oo3x0bt4e8mjpp75 
        foreign key (sectores_id) 
        references sectores

    alter table sectores_postulantes 
        add constraint FK_a4o1a5juef69iasqps0rb4m4 
        foreign key (postulantes_id) 
        references miembros

    alter table sectores_postulantes 
        add constraint FK_jg1ccn37xxgwyotjmdk1l73d1 
        foreign key (sectores_id) 
        references sectores

    alter table tramos 
        add constraint FK_35wd8ujv1xscpx0mvsh63tsan 
        foreign key (medioDeTransporte_id) 
        references MedioDeTransporte

    alter table tramos 
        add constraint FK_4mxgidtq13bmlbwhifku3u451 
        foreign key (trayectoId) 
        references trayectos

    create sequence hibernate_sequence
