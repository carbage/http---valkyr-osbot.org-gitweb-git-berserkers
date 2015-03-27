package org.valkyr.api.framework.webwalker.web;

import com.google.common.io.Files;
import org.osbot.rs07.api.map.Position;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TextIO {

    public static final String DIRECTORY = System.getProperty("user.home") + "/OSBot/Resources/";
    public static final String WEB = DIRECTORY + "web.txt";
    public static final String SEPARATOR = ",";
    public static final String EDGE_SEPARATOR = ";";
    public static final String LINE_BREAK = "\n";

    public static final int X = 0;
    public static final int Y = 1;
    public static final int Z = 2;
    public static final int NAME = 3;
    public static final int OPTIONS = 4;

    public static Web readFile() {
        Downloader.downloadWeb();
        System.out.println("WebWalker: Loading web...");
        Web web = new Web();
        try {
            readSet(WEB, web);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return web;
    }


    public static void writeFile(Web web) {
        try {
            writeSet(web.getAllNodes(), WEB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeSet(ArrayList<WebNode> set, String file) throws IOException {
        Map<WebNode, WebNode> written = new HashMap<>();
        FileOutputStream out = new FileOutputStream(file);
        for (WebNode node : set)
            for (WebNode edge : node.getEdges()) {
                if (written.get(node) != edge && written.get(edge) != node) {
                    written.put(node, edge);
                    out.write((getNodeString(edge) + EDGE_SEPARATOR + getNodeString(node) + LINE_BREAK).getBytes());
                }
            }
        out.close();
    }

    public static void readSet(String file, Web web) throws IOException {
        for (String s : Files.readLines(new File(file), Charset.defaultCharset())) {
            WebNode a = null;
            WebNode b = null;
            String[] pair = s.split(EDGE_SEPARATOR);
            for (String edge : pair) {
                edge = edge.replace(EDGE_SEPARATOR, "");
                String[] nodeString = edge.split(SEPARATOR);
                WebNode node = parseNode(nodeString, web);
                if (node != null) {
                    if (a == null) {
                        a = node;
                    } else {
                        b = node;
                    }
                }
                if (a != null && b != null)
                    web.addEdge(a, b);
            }
        }
    }

    public static String getNodeString(WebNode n) {
        StringBuilder sb = new StringBuilder();
        sb.append(n.getX() + SEPARATOR);
        sb.append(n.getY() + SEPARATOR);
        sb.append(n.getZ() + SEPARATOR);
        sb.append(n.getName() + SEPARATOR);
        for (String s : n.getInteractOptions())
            if (s != null && !s.equals("null"))
                sb.append(s + SEPARATOR);
        return sb.toString();

    }

    public static WebNode parseNode(String[] nodeString, Web web) {
        try {
            int x = 0, y = 0, z = 0;
            String name = "";
            ArrayList<String> options = new ArrayList<>();
            for (int i = 0; i < nodeString.length; i++) {
                if (i == X)
                    x = Integer.valueOf(nodeString[i]);
                if (i == Y)
                    y = Integer.valueOf(nodeString[i]);
                if (i == Z)
                    z = Integer.valueOf(nodeString[i]);
                if (i == NAME)
                    name = nodeString[i];
                if (i >= OPTIONS)
                    if (!nodeString[i].equals("null"))
                        options.add(nodeString[i]);
            }
            WebNode n = new WebNode(new Position(x, y, z), name, options.toArray(new String[options.size()]));
            n = web.addNew(n);
            return n;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static File getFile() {
        return new File(WEB);
    }

    public static boolean fileExists() {
        return getFile().exists();
    }
}
