package ua.lil.proxy.commands;

import ua.lil.proxy.Core;
import ua.lil.proxy.api.command.Command;
import ua.lil.proxy.api.command.CommandSender;
import ua.lil.proxy.api.command.ConsoleSender;

public class StopCommand implements Command {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ConsoleSender) {
            sender.sendMessage("Stopping the core...");
            Core.getInstance().stop();
        } else {
            sender.sendMessage("\u00a7c[CORE] \u0414\u0430\u043d\u043d\u0430\u044f \u043a\u043e\u043c\u0430\u043d\u0434\u0430 \u043c\u043e\u0436\u0435\u0442 \u0431\u044b\u0442\u044c \u0432\u044b\u043f\u043e\u043b\u043d\u0435\u043d\u0430 \u0442\u043e\u043b\u044c\u043a\u043e \u0447\u0435\u0440\u0435\u0437 \u043a\u043e\u043d\u0441\u043e\u043b\u044c!");
        }
    }
}

