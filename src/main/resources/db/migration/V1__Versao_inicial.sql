create sequence documents.seq_documento increment by 1 start with 1 nomaxvalue minvalue 1 nocycle nocache;
create table documents.documento (seq_documento number not null, num_conteudo varchar2(24) not null, qtd_paginas number(5, 0), tamanho number(10,0) not null, constraint pk_documento primary key (seq_documento));

create sequence documents.seq_certificado_digital increment by 1 start with 1 nomaxvalue minvalue 1 nocycle nocache;
create table documents.certificado_digital (seq_certificado_digital number not null, dsc_certificado_digital varchar2(768) not null, cod_serial varchar2(60) not null, txt_certificado_digital clob not null, dat_validade_inicial date not null, dat_validade_final date not null, seq_certificado_emissor number not null, tip_certificado_digital varchar2(1) not null, tip_pki varchar2(15) not null, constraint pk_certificado_digital primary key (seq_certificado_digital));
alter table documents.certificado_digital add constraint uk_cod_serial_cedi unique(cod_serial, seq_certificado_emissor);
alter table documents.certificado_digital add constraint fk_certificado_digital_cedi foreign key (seq_certificado_emissor) references documents.certificado_digital;
alter table documents.certificado_digital add constraint ck_tip_certificado_digita_cedi check (tip_certificado_digital in ('A', 'F', 'R'));
alter table documents.certificado_digital add constraint ck_tip_pki_cedi check (tip_pki in ('ICP_BRASIL'));

create sequence documents.seq_tipo_documento increment by 1 start with 1 nomaxvalue minvalue 1 nocycle nocache;
create table documents.tipo_documento (seq_tipo_documento number not null, dsc_tipo_documento varchar2(100) not null, constraint pk_tipo_documento primary key (seq_tipo_documento));
alter table documents.tipo_documento add constraint uk_timo_dsc_tipo_documento unique (dsc_tipo_documento);

create sequence documents.seq_modelo increment by 1 start with 1 nomaxvalue minvalue 1 nocycle nocache;
create table documents.modelo (seq_modelo number not null, nom_modelo varchar2(100) not null, seq_tipo_documento number not null, seq_documento_template number not null, constraint pk_modelo primary key (seq_modelo));
alter table documents.modelo add constraint fk_documento_mode foreign key (seq_documento_template) references documents.documento(seq_documento);
alter table documents.modelo add constraint fk_tipo_documento_mode foreign key (seq_tipo_documento) references documents.tipo_documento(seq_tipo_documento);
alter table documents.modelo add constraint uk_mode_seq_tipo_documento  unique (seq_tipo_documento, nom_modelo);

create sequence documents.seq_texto increment by 1 start with 1 nomaxvalue minvalue 1 nocycle nocache;
create table documents.texto (seq_texto number not null, seq_documento number not null, seq_documento_final number, constraint pk_texto primary key (seq_texto));
alter table documents.texto add constraint fk_docu_documento_text foreign key (seq_documento) references documents.documento(seq_documento);
alter table documents.texto add constraint fk_docu_documento_final_text foreign key (seq_documento_final) references documents.documento(seq_documento);