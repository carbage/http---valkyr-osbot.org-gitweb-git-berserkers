package alpharunescape.src;

import alpharunescape.src.task.Task;
import alpharunescape.src.task.Tasks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlphaRunescapeGui extends JFrame {

    private AlphaRunescape script;

    private JPanel main = new JPanel();
    private JPanel taskPanel = new JPanel();
    private JPanel settingsPanel = new JPanel();
    private JComboBox<Tasks> taskComboBox = new JComboBox<>(Tasks.values());
    private JPanel taskListPanel = new JPanel();
    private JScrollPane taskListScrollPane = new JScrollPane(taskListPanel);

    private Task selectedTask = null;

    public AlphaRunescapeGui(AlphaRunescape script) {
        this.script = script;

        setTitle(script.getName());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setContentPane(main);

        main.setBorder(BorderFactory.createTitledBorder("Tasks"));
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        taskPanel.setLayout(new FlowLayout());
        taskComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTask();
                updateSettings();
            }
        });

        JButton addTask = new JButton("Add Task");
        addTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        settingsPanel.setBorder(BorderFactory.createTitledBorder("Settings"));
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));

        taskListPanel.setPreferredSize(new Dimension(400, 300));

        taskPanel.add(taskComboBox);
        taskPanel.add(addTask);
        taskPanel.add(settingsPanel);

        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));


        main.add(taskPanel);
        main.add(taskListScrollPane);
        //main.add(taskList);
        updateTask();
        updateSettings();

        pack();
        setVisible(true);
    }

    public void updateTask() {
        selectedTask = ((Tasks) taskComboBox.getSelectedItem()).getTask(script);
    }

    private void addTask() {
        if (selectedTask != null) {
            script.getScheduler().addTask(selectedTask);
            selectedTask = ((Tasks) taskComboBox.getSelectedItem()).getTask(script);
            updateSettings();
            refreshTasks();
        }
    }

    public void updateSettings() {
        settingsPanel.removeAll();
        for (Component c : selectedTask.getSettingsPanel().getComponents())
            settingsPanel.add(c);
        refresh();
    }

    private void refresh() {
        getContentPane().invalidate();
        getContentPane().revalidate();
        getContentPane().repaint();
        settingsPanel.invalidate();
        settingsPanel.revalidate();
        settingsPanel.repaint();
        taskPanel.invalidate();
        taskPanel.revalidate();
        taskPanel.repaint();
        taskListScrollPane.invalidate();
        taskListScrollPane.revalidate();
        taskListScrollPane.repaint();
        taskListPanel.invalidate();
        taskListPanel.revalidate();
        taskListPanel.repaint();
        invalidate();
        revalidate();
        repaint();
        pack();
    }

    public void refreshTasks() {
        taskListPanel.removeAll();
        for (Task t : script.getScheduler().getTasks())
            taskListPanel.add(t.getTaskPanel());
        refresh();
    }

}
