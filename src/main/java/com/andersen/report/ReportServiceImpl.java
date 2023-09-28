package com.andersen.report;

import com.andersen.track.service.TrackResponseDto;
import com.andersen.track.service.TrackService;
import com.andersen.track.service.TrackServiceImpl;
import com.andersen.user.service.UserResponseDto;
import com.andersen.user.service.UserService;
import com.andersen.user.service.UserServiceImpl;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class ReportServiceImpl implements ReportService{

    private static ReportService reportService;
    private TrackService trackService = TrackServiceImpl.getInstance();
    private UserService userService = UserServiceImpl.getInstance();

    public static ReportService getInstance() {
        if (reportService == null) {
            reportService = new ReportServiceImpl();
        }
        return reportService;
    }

    @Override
    public String createDailyReport(LocalDate localDate, String reportName) {
        List<TrackResponseDto> tracks = trackService.getByData(localDate);

        Document doc = new Document();
        PdfWriter writer = null;

        try {
            writer = PdfWriter.getInstance(doc, new FileOutputStream(reportName));
            doc.open();
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            PdfPCell cellName = new PdfPCell(new Paragraph("Name"));
            PdfPCell cellLastName = new PdfPCell(new Paragraph("Lastname"));
            PdfPCell cellTime = new PdfPCell(new Paragraph("Time"));
            PdfPCell cellDescription = new PdfPCell(new Paragraph("Description"));
            table.addCell(cellName);
            table.addCell(cellLastName);
            table.addCell(cellTime);
            table.addCell(cellDescription);

            for (TrackResponseDto track : tracks){
                UserResponseDto user = userService.getById(track.getUserId());
                PdfPCell cellNameTrack = new PdfPCell(new Paragraph(user.getName()));
                PdfPCell cellLastNameTrack = new PdfPCell(new Paragraph(user.getLastname()));
                PdfPCell cellTimeTrack = new PdfPCell(new Paragraph(String.valueOf(track.getTime())));
                PdfPCell cellDescriptionTrack = new PdfPCell(new Paragraph(track.getNote()));
                table.addCell(cellNameTrack);
                table.addCell(cellLastNameTrack);
                table.addCell(cellTimeTrack);
                table.addCell(cellDescriptionTrack);
            }

            doc.add(table);
            return reportName;
        } catch (DocumentException | FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            doc.close();
            if (writer != null) {
                writer.close();
            }
        }
    }
}
