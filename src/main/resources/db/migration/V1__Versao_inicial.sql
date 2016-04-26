create schema documento;

create sequence documento.SEQ_DOCUMENTO increment by 1 start with 1 nomaxvalue minvalue 1 nocycle nocache;

create table documento.documento (seq_documento bigint not null, num_conteudo varchar2(24) not null, qtd_paginas number(5, 0), tamanho number(10,0) not null, constraint pk_documento primary key (seq_documento));

