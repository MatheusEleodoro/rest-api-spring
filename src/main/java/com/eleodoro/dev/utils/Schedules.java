package com.eleodoro.dev.utils;

import com.eleodoro.dev.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Schedules
{
    Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Scheduled(cron = "0 0 5 * * *")
    private void reloadRepositorios()
    {
        logger.info("Reiniciando dados de repositorios");
        UsuarioService.gitRepositorios = new ArrayList<>();
    }
}
