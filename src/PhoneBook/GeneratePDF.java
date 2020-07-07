package PhoneBook;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import javafx.collections.ObservableList;

public class GeneratePDF {
    public void generatePDF(String fileNameFx, ObservableList<Person> dataFx) {
        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(fileNameFx + ".pdf"));
            doc.open();
            //Logo
            Image img = Image.getInstance("C:/Users/hazázs/Documents/NetBeansProjects/ThePhoneBook/src/logo.jpg");
            img.scaleToFit(400, 172);
            img.setAbsolutePosition(170f, 650f);
            doc.add(img);
            //Line breaks
            doc.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n"));
            //Table
            float[] columnWidths = {1, 4, 4, 6};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell(new Phrase("Contacts"));
            cell.setBackgroundColor(GrayColor.GRAYWHITE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(4);
            table.addCell(cell);
            table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell("ID");
            table.addCell("Last Name");
            table.addCell("First Name");
            table.addCell("E-mail");
            table.setHeaderRows(1);
            table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            for (int i = 0; i < dataFx.size(); i++) {
                table.addCell(String.valueOf(i + 1));
                table.addCell(dataFx.get(i).getLastName());
                table.addCell(dataFx.get(i).getFirstName());
                table.addCell(dataFx.get(i).getEmail());
            }
            doc.add(table);
            //Signature
            doc.add(new Paragraph(new Chunk("Created by the Phone Book application.")));
        } catch (Exception e) {
            System.out.println(e);
          }
        doc.close();
    }
}