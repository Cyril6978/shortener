package com.shortener.shortener.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import com.shortener.shortener.entity.Error;

public class LoggingController {
    Logger logger = LoggerFactory.getLogger(LoggingController.class);



    public void generateErrorMessage(Error error) {
        //méthode> <chemin HTTP> from <addresse IP source>, <type de l'erreur>: <message d'erreur> (<fichier source> => <ligne de code>)

        logger.error(error.getMethod() + " " + error.getPathHttp() + " from " + error.getAdressIp() + ", "
                + error.getTypeOfError() + ":" + error.getMessageError() + " (" + error.getFileSrc() + " => " + error.getLine() + ")");

    }
}
