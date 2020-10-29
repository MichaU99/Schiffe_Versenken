package swingtest;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Erstes Beispiel zur Verwendung von (AWT und) Swing.
class Swing1 {
    // Graphische Oberfläche aufbauen und anzeigen.
    private static void start () {
	// Hauptfenster mit Titelbalken etc. (JFrame) erzeugen.
	// "swingtest.Swing1" wird in den Titelbalken geschrieben.
	JFrame frame = new JFrame("SCHIFFE VERSENKEN");

	// Beim Schließen des Fensters (z. B. durch Drücken des
	// X-Knopfs in Windows) soll das Programm beendet werden.
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	// Der Inhalt des Fensters soll von einem BoxLayout-Manager
	// verwaltet werden, der seine Bestandteile vertikal (von
	// oben nach unten) anordnet.
	frame.setContentPane(Box.createVerticalBox());

	// Dehnbaren Zwischenraum am oberen Rand hinzufügen.
	frame.add(Box.createGlue());

	// Darunter ein horizontal zentriertes "Etikett" (JLabel)
	// hinzufügen.
	JLabel label = new JLabel("Schiffe versenken");
	label.setAlignmentX(Component.CENTER_ALIGNMENT);
	frame.add(label);

	// Festen Zwischenraum der Größe 50 Pixel hinzufügen.
	frame.add(Box.createVerticalStrut(50));

	// Horizontal zentrierten Knopf (JButton) hinzufügen.
	// Beim Drücken des Knopfs wird die an addActionListener
	// übergebene anonyme Funktion (e) -> { ...... } aufgerufen,
	// die einen Parameter des Typs ActionEvent besitzen muss,
	// der hier aber nicht verwendet wird und dessen Typ auch nicht
	// explizit angegeben werden muss.
	JButton button = new JButton("Einstellungen");
	JButton spiel_vs_ki =new JButton("Spiele vs. KI");
	JButton netzwerkspiel =new JButton("Netzwerkspiel");
	JButton beenden_1 =new JButton("Beenden");
	button.setAlignmentX(Component.CENTER_ALIGNMENT);
	spiel_vs_ki.setAlignmentX(Component.CENTER_ALIGNMENT);
	netzwerkspiel.setAlignmentX(Component.CENTER_ALIGNMENT);
	beenden_1.setAlignmentX(Component.CENTER_ALIGNMENT);
	button.addActionListener(
	    (e) -> { System.out.println("Knopf gedrückt"); }
	);
	frame.add(spiel_vs_ki);
	frame.add(Box.createVerticalStrut(10));
	frame.add(netzwerkspiel);
	frame.add(Box.createVerticalStrut(10));
	frame.add(button);
	frame.add(Box.createVerticalStrut(10));
	frame.add(beenden_1);

	// Festen Zwischenraum der Größe 50 Pixel hinzufügen.
	frame.add(Box.createVerticalStrut(50));

	// Horizontale Box hinzufügen, die ihrerseits aus drei
	// "Etiketten" (JLabel) besteht, die jeweils ein Piktogramm
	// (ImageIcon) enthalten. Dehnbarer Zwischenraum vor und nach
	// den "Etiketten" sorgt für eine gleichmäßige horizontale
	// Verteilung innerhalb der Box.
	Box box = Box.createHorizontalBox();
	{
	    Icon green = new ImageIcon("green.png");
	    Icon yellow = new ImageIcon("yellow.png");
	    Icon red = new ImageIcon("red.png");
		Icon map = new ImageIcon("map.png");
	    box.add(Box.createGlue());
	    box.add(new JLabel(green));
	    box.add(Box.createGlue());
	    box.add(new JLabel(yellow));
	    box.add(Box.createGlue());
	    box.add(new JLabel(red));
		box.add(Box.createGlue());
		box.add(new JLabel(map));
		box.add(Box.createGlue());
	}
	frame.add(box);

	// Dehnbaren Zwischenraum am unteren Rand hinzufügen.
	frame.add(Box.createGlue());

	// Menüzeile (JMenuBar) erzeugen und einzelne Menüs (JMenu)
	// mit Menüpunkten (JMenuItem) hinzufügen.
	// Jeder Menüpunkt ist eigentlich ein Knopf, dem wie oben
	// eine anonyme Funktion zugeordnet werden kann.
	// (Hier exemplarisch nur für einen Menüpunkt.)
	JMenuBar bar = new JMenuBar();
	{
	    JMenu menu = new JMenu("File");
	    {
		JMenuItem item = new JMenuItem("Open");
		item.addActionListener(
		    (e) -> { System.out.println("File -> Open"); }
		);
		menu.add(item);
	    }
	    {
		JMenuItem item = new JMenuItem("Save");
		menu.add(item);
	    }
	    bar.add(menu);
	}
	{
	    JMenu menu = new JMenu("Edit");
	    {
		JMenuItem item = new JMenuItem("Copy");
		menu.add(item);
	    }
	    {
		JMenuItem item = new JMenuItem("Paste");
		menu.add(item);
	    }
	    bar.add(menu);
	}

	// Menüzeile zum Fenster hinzufügen.
	frame.setJMenuBar(bar);

	// Am Schluss (!) die optimale Fenstergröße ermitteln (pack)
	// und das Fenster anzeigen (setVisible).
	frame.pack();
	frame.setVisible(true);
    }

    // Hauptprogramm.
    public static void main (String [] args) {
	// Laut Swing-Dokumentation sollte die graphische Oberfläche
	// nicht direkt im Hauptprogramm (bzw. im Haupt-Thread) erzeugt
	// und angezeigt werden, sondern in einem von Swing verwalteten
	// separaten Thread.
	// Hierfür wird der entsprechende Code in eine parameterlose
	// anonyme Funktion () -> { ...... } "verpackt", die an
	// SwingUtilities.invokeLater übergeben wird.
	SwingUtilities.invokeLater(
	    () -> { start(); }
	);
    }
}
