package insys;


import java.io.*;
import java.util.List;
import javax.swing.*;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import jxl.*;

public class export_excel1 {

	    private File archi;
	    private List<JTable> tabla;
            private List<Integer> NumCol;
	    private WritableCellFormat	fomato_fila ;
	    private WritableCellFormat	fomato_columna;
            
            
	    public export_excel1(List<JTable> tab, File ar, List<Integer> Num) throws Exception {
	        
                this.NumCol=Num;
                this.archi = ar;
	        this.tabla = tab;
	        if(tab.size()<0){
	            throw new Exception("ERROR");
	        }
	    }

	    public boolean export() {
	        try {
	            DataOutputStream out = new DataOutputStream(new FileOutputStream(archi));
	            WritableWorkbook w = Workbook.createWorkbook(out);
                    w.createSheet("Reporte", 0);
                    //w.createSheet("Recursos", 1);
                    //w.createSheet("Responsables", 2);
                    
                    
                    System.out.println("Numero de Tablas: "+tabla.size());
                    
	            for (int index=0;index<tabla.size();index++) {
                        JTable table=tabla.get(index);
	                WritableSheet s = w.getSheet(index);
                        
                        //System.out.println("TablaSeleccionada: "+tabla.get(index));
                        //System.out.println("PestañaSeleccionada: "+w.getSheet(index));
                        
                        //int TamanoCol=70;
                        
                        s.setColumnView(0, 20);//Aumento de tamaño de columna 0
                        s.setColumnView(1, 100);
                        s.setColumnView(2, 20);
                        s.setColumnView(3, 40);
                        s.setColumnView(4, 40);
                        s.setColumnView(5, 40);
                        
                        
	                for (int i = 0; i <= NumCol.get(index); i++) {
	                    for (int j = 0; j < table.getRowCount(); j++) {
	                        System.out.println("ColumnaSeleccionada: "+i+" FilaSeleccionada: "+j);
                                Object objeto = table.getValueAt(j, i);
                                //Cambiar las palabras true y false
                                String convertedToString = String.valueOf(objeto); 
                                if (convertedToString.equals("null")){
                                    convertedToString=" ";
                                }
                                
                                objeto=convertedToString;  
                                
	                        createColumna(s,table.getColumnName(i),i);//crea la columna
	                        createFilas(s,i,j+1,String.valueOf(objeto));//crea las filas
	                       
	                    }
                            
	                }
                        
	            }
                    
                    
	            w.write();
	            w.close();
	            out.close();
	            return true;

	        } catch (IOException ex) {
	            ex.printStackTrace();
	        } catch (WriteException ex) {
	            ex.printStackTrace();
	        }
	        return false;
	    }
	    private void createColumna(WritableSheet sheet,String columna,int number_columna)throws WriteException {
			//creamos el tipo de letra
			WritableFont times10pt = new WritableFont(WritableFont.TAHOMA, 14);
			// definimos el formato d ela celda
			WritableCellFormat	times = new WritableCellFormat(times10pt);
			// Permite si se ajusta autom�ticamente a las c�lulas
			times.setWrap(true);
			// crea una negrita con subrayado //11
			WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TAHOMA, 11, WritableFont.BOLD, false,UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
                        WritableCellFormat tableFormatBackground = new WritableCellFormat(); //table cell format
                        tableFormatBackground.setBackground(Colour.DARK_BLUE2) ; //Table background
                        tableFormatBackground.setBorder(Border.ALL, BorderLineStyle.THIN,Colour.BLACK); //table border style
                        tableFormatBackground.setFont(times10ptBoldUnderline); //set the font
                        tableFormatBackground.setAlignment(Alignment.CENTRE);// set alignment left
                        
			fomato_columna = new WritableCellFormat(tableFormatBackground);
                        
                        
			// Permite que se ajusta autom�ticamente a las c�lulas
			fomato_columna.setWrap(true);
                        fomato_columna.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			CellView cevell = new CellView();
                        cevell.setSize(1200);
			cevell.setFormat(times);
                        cevell.setFormat(fomato_columna);
			cevell.setAutosize(true);
			// escribimos las columnas
			addColumna(sheet, number_columna, 0, columna,fomato_columna);//numero de columna , 0 es la fila
		}
	    /****************************************/
	    private void createFilas(WritableSheet sheet,int number_columna,int filas,String name_filas)throws WriteException {
			//creamos el tipo de letra
			WritableFont times10pt = new WritableFont(WritableFont.ARIAL, 12);
			times10pt.setColour(Colour.GOLD);
			// definimos el formato d ela celda
			WritableCellFormat times = new WritableCellFormat(times10pt);
			times.setBorder(Border.TOP, BorderLineStyle.MEDIUM, Colour.GOLD);
			// Permite si se ajusta autom�ticamente a las c�lulas
			times.setWrap(true);
			// crea una negrita con subrayado  12
			WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD, false,UnderlineStyle.NO_UNDERLINE);
			fomato_fila = new WritableCellFormat(times10ptBoldUnderline);
			// Permite que se ajusta autom�ticamente a las c�lulas
			fomato_fila.setWrap(true);
                        fomato_fila.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                        
			CellView cevell = new CellView();
			cevell.setDimension(70);
                        cevell.setFormat(times);
			cevell.setFormat(fomato_fila);
			cevell.setAutosize(true);
			// escribimos las columnas
			addFilas(sheet, number_columna, filas, name_filas,fomato_fila);
		}
	   
	    
	    /***********************************/
	    private void addColumna(WritableSheet sheet, int column, int row, String s,WritableCellFormat format)throws RowsExceededException, WriteException {
			Label label;
			label = new Label(column, row, s, format);
			sheet.addCell(label);
		}
	    private void addFilas(WritableSheet sheet, int column, int row, String s,WritableCellFormat format)throws WriteException, RowsExceededException {
			Label label;
			label = new Label(column, row, s, format);
			sheet.addCell(label);
		}
            
            

	
}
