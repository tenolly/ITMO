package server.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import server.handlers.ServerEnviroment;
import utils.commands.Command;
import utils.commands.types.OnlyForServer;

/**
* Saves collection to the file
*/
public class Save extends Command implements OnlyForServer {
    private String filename;

    public Save(String name, String description, String filename) {
        super(name, description);
        this.filename = filename;
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        ArrayList<String> output = new ArrayList<String>();

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        xmlMapper.findAndRegisterModules();
        try {
            xmlMapper.writeValue(new File(filename), ((ServerEnviroment) enviroment).getCollectionHandler().getCollection());
            output.add("Collection saved to " + filename);
        } catch (IOException e) {
            output.add("Failed");
        }

        return output;
    }
    
}
