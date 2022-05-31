package com.eleodoro.dev.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultStatus
{
    private boolean erro;
    private String mensagem;
    private int statusCode;
}
