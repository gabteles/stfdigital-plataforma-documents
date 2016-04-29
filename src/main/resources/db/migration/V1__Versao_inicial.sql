create schema documento;

create sequence documento.SEQ_DOCUMENTO increment by 1 start with 1 nomaxvalue minvalue 1 nocycle nocache;
create table documento.documento (seq_documento bigint not null, num_conteudo varchar2(24) not null, qtd_paginas number(5, 0), tamanho number(10,0) not null, constraint pk_documento primary key (seq_documento));

create sequence documento.seq_certificado_digital increment by 1 start with 1 nomaxvalue minvalue 1 nocycle nocache;
create table documento.certificado_digital (seq_certificado_digital bigint not null, dsc_certificado_digital varchar2(768) not null, cod_serial varchar2(60) not null, txt_certificado_digital clob not null, dat_validade_inicial date not null, dat_validade_final date not null, seq_certificado_emissor bigint not null, tip_certificado_digital varchar2(1) not null, tip_pki varchar2(15) not null, constraint pk_certificado_digital primary key (seq_certificado_digital));
alter table documento.certificado_digital add constraint uk_cod_serial_cedi unique(cod_serial, seq_certificado_emissor);
alter table documento.certificado_digital add constraint fk_certificado_digital_cedi foreign key (seq_certificado_emissor) references documento.certificado_digital;
alter table documento.certificado_digital add constraint ck_tip_certificado_digital_cedi check (tip_certificado_digital in ('A', 'F', 'R'));
alter table documento.certificado_digital add constraint ck_tip_pki_cedi check (tip_pki in ('ICP_BRASIL'));