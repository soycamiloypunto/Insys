/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package insys;

/**
 *
 * @author cktv
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class FormatoTabla2 extends DefaultTableCellRenderer {
  Font normal = new Font("Arial", 0, 12);
  
  Font negrilla = new Font("Helvetica", 1, 18);
  
  Font cursiva = new Font("Times new roman", 2, 12);
  
  public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
    setEnabled((table == null || table.isEnabled()));
    setBackground(Color.white);
    table.setForeground(Color.black);
    if (String.valueOf(table.getValueAt(row, 5)).equals("Si")) {
      setBackground(new Color(153, 100, 151));
      table.setForeground(Color.white);
    } else if (String.valueOf(table.getValueAt(row, 7)).equals("No")) {
      setBackground(Color.RED);
      table.setForeground(Color.white);
    } else if (String.valueOf(table.getValueAt(row, 0)).equals("Titular")) {
      table.setForeground(Color.blue);
    } 
    super.getTableCellRendererComponent(table, value, selected, focused, row, column);
    return this;
  }
  
  private boolean isNumber(String cadena) {
    try {
      Double.parseDouble(cadena.replace(",", ""));
    } catch (NumberFormatException nfe) {
      String newCadena = cadena.replace(".", "").replace(',', '.');
      try {
        Double.parseDouble(newCadena);
      } catch (NumberFormatException nfe2) {
        return false;
      } 
    } 
    return true;
  }
}
