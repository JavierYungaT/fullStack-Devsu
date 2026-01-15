package banco.ms_cuenta_movimiento.service.impl;

import banco.ms_cuenta_movimiento.dto.reports.ClientReportDTO;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfServiceImpl {

    public byte[] generateReportPdf(ClientReportDTO report, String initialDate, String finalDate) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Título
            Paragraph title = new Paragraph("ESTADO DE CUENTA")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Información del cliente
            document.add(new Paragraph("Cliente: " + report.getClientName()));
            document.add(new Paragraph("Identificación: " + report.getClientDni()));
            document.add(new Paragraph("Período: " + initialDate + " - " + finalDate));
            document.add(new Paragraph("\n"));

            // Tabla de movimientos
            float[] columnWidths = {2, 2, 2, 2, 2, 1, 2, 2};
            Table table = new Table(columnWidths);

            // Headers
            table.addHeaderCell("Fecha");
            table.addHeaderCell("Cliente");
            table.addHeaderCell("Número Cuenta");
            table.addHeaderCell("Tipo");
            table.addHeaderCell("Saldo Inicial");
            table.addHeaderCell("Estado");
            table.addHeaderCell("Movimiento");
            table.addHeaderCell("Saldo Disponible");

            // Datos
            report.getAccounts().forEach(account -> {
                account.getMovements().forEach(movement -> {
                    table.addCell(movement.getMovementDate());
                    table.addCell(report.getClientName());
                    table.addCell(account.getAccountNumber());
                    table.addCell(account.getAccountType());
                    table.addCell("$" + account.getBalance());
                    table.addCell("True");
                    table.addCell("$" + movement.getMovementAmount());
                    table.addCell("$" + movement.getMovementBalance());
                });
            });

            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }
}
