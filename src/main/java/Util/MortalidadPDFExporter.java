package Util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;

import DAO.MortalidadDAO;
import Modelo.ControlMortalidad;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Exportador de reportes PDF para control de mortalidad.
 * Genera reportes profesionales con estadísticas y tablas detalladas.
 * 
 * @author franc
 */
public class MortalidadPDFExporter {

  // Colores corporativos del sistema avícola
  private static final DeviceRgb COLOR_PRIMARY = new DeviceRgb(45, 90, 39); // Verde principal
  private static final DeviceRgb COLOR_ACCENT = new DeviceRgb(255, 140, 66); // Naranja acento
  private static final DeviceRgb COLOR_LIGHT_GRAY = new DeviceRgb(245, 245, 245); // Gris claro
  private static final DeviceRgb COLOR_DARK_GRAY = new DeviceRgb(80, 80, 80); // Gris oscuro

  /**
   * Exporta el reporte de mortalidad a un archivo PDF.
   * 
   * @param destinationPath Ruta donde guardar el archivo PDF
   * @throws Exception Si ocurre un error durante la generación
   */
  public static void exportarReporte(String destinationPath) throws Exception {
    // Obtener datos de mortalidad
    List<ControlMortalidad> mortalidades = MortalidadDAO.obtenerTodasLasMortalidades();

    // Crear el documento PDF
    PdfWriter writer = new PdfWriter(destinationPath);
    PdfDocument pdf = new PdfDocument(writer);
    Document document = new Document(pdf);

    // Configurar fuentes
    PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
    PdfFont fontRegular = PdfFontFactory.createFont(StandardFonts.HELVETICA);

    try {
      // Encabezado del reporte
      agregarEncabezado(document, fontBold, fontRegular);

      // Estadísticas generales
      agregarEstadisticas(document, mortalidades, fontBold, fontRegular);

      // Tabla detallada
      agregarTablaDetallada(document, mortalidades, fontBold, fontRegular);

      // Gráfico por granja (resumen)
      agregarResumenPorGranja(document, mortalidades, fontBold, fontRegular);

      // Pie de página
      agregarPiePagina(document, fontRegular);

    } finally {
      document.close();
    }
  }

  /**
   * Agrega el encabezado profesional del reporte.
   */
  private static void agregarEncabezado(Document document, PdfFont fontBold, PdfFont fontRegular) {
    // Título principal
    Paragraph titulo = new Paragraph("REPORTE DE CONTROL DE MORTALIDAD")
        .setFont(fontBold)
        .setFontSize(20)
        .setFontColor(COLOR_PRIMARY)
        .setTextAlignment(TextAlignment.CENTER)
        .setMarginBottom(5);

    document.add(titulo);

    // Subtítulo con fecha
    String fechaGeneracion = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    Paragraph subtitulo = new Paragraph("Sistema Integrador Avícola - Generado el " + fechaGeneracion)
        .setFont(fontRegular)
        .setFontSize(12)
        .setFontColor(COLOR_DARK_GRAY)
        .setTextAlignment(TextAlignment.CENTER)
        .setMarginBottom(20);

    document.add(subtitulo);

    // Línea separadora (usando tabla como línea)
    Table lineTable = new Table(1).setWidth(UnitValue.createPercentValue(100));
    lineTable.addCell(new Cell().setHeight(3).setBackgroundColor(COLOR_ACCENT).setBorder(Border.NO_BORDER));
    document.add(lineTable);
    document.add(new Paragraph().setMarginBottom(15));
  }

  /**
   * Agrega las estadísticas generales del reporte.
   */
  private static void agregarEstadisticas(Document document, List<ControlMortalidad> mortalidades,
      PdfFont fontBold, PdfFont fontRegular) {
    // Título de sección
    Paragraph tituloStats = new Paragraph("ESTADÍSTICAS GENERALES")
        .setFont(fontBold)
        .setFontSize(14)
        .setFontColor(COLOR_PRIMARY)
        .setMarginBottom(10);
    document.add(tituloStats);

    // Calcular estadísticas
    int totalRegistros = mortalidades.size();
    int totalMortalidad = mortalidades.stream()
        .mapToInt(ControlMortalidad::getCantidad)
        .sum();

    long granjas = mortalidades.stream()
        .map(m -> m.getGranja().getId())
        .distinct()
        .count();

    // Mortalidad por sexo
    Map<String, Integer> mortalidadPorSexo = mortalidades.stream()
        .collect(Collectors.groupingBy(
            ControlMortalidad::getSexo,
            Collectors.summingInt(ControlMortalidad::getCantidad)));

    // Crear tabla de estadísticas
    Table statsTable = new Table(UnitValue.createPercentArray(new float[] { 30, 70 }))
        .setWidth(UnitValue.createPercentValue(100))
        .setMarginBottom(20);

    // Estilo para celdas de estadísticas
    statsTable
        .addCell(crearCeldaEstadistica("Total de Registros:", String.valueOf(totalRegistros), fontBold, fontRegular));
    statsTable.addCell(crearCeldaEstadistica("Total de Mortalidad:", String.valueOf(totalMortalidad) + " aves",
        fontBold, fontRegular));
    statsTable.addCell(crearCeldaEstadistica("Granjas Involucradas:", String.valueOf(granjas), fontBold, fontRegular));
    statsTable.addCell(crearCeldaEstadistica("Mortalidad Machos:",
        String.valueOf(mortalidadPorSexo.getOrDefault("M", 0)) + " aves", fontBold, fontRegular));
    statsTable.addCell(crearCeldaEstadistica("Mortalidad Hembras:",
        String.valueOf(mortalidadPorSexo.getOrDefault("F", 0)) + " aves", fontBold, fontRegular));

    document.add(statsTable);
  }

  /**
   * Crea una celda para la tabla de estadísticas.
   */
  private static Cell crearCeldaEstadistica(String label, String value, PdfFont fontBold, PdfFont fontRegular) {
    Paragraph p = new Paragraph()
        .add(new Text(label).setFont(fontBold).setFontColor(COLOR_DARK_GRAY))
        .add(new Text(" " + value).setFont(fontRegular));

    return new Cell().add(p)
        .setBorder(Border.NO_BORDER)
        .setPadding(5);
  }

  /**
   * Agrega la tabla detallada con todos los registros.
   */
  private static void agregarTablaDetallada(Document document, List<ControlMortalidad> mortalidades,
      PdfFont fontBold, PdfFont fontRegular) {
    // Título de sección
    Paragraph tituloTabla = new Paragraph("REGISTRO DETALLADO DE MORTALIDAD")
        .setFont(fontBold)
        .setFontSize(14)
        .setFontColor(COLOR_PRIMARY)
        .setMarginBottom(10)
        .setMarginTop(20);
    document.add(tituloTabla);

    // Crear tabla
    float[] columnWidths = { 8, 12, 25, 10, 12, 8, 15, 10 }; // Porcentajes
    Table table = new Table(UnitValue.createPercentArray(columnWidths))
        .setWidth(UnitValue.createPercentValue(100));

    // Encabezados
    String[] headers = { "ID", "Fecha", "Granja", "Galpón", "Cantidad", "Sexo", "Lote", "% Mortalidad" };
    for (String header : headers) {
      Cell headerCell = new Cell()
          .add(new Paragraph(header).setFont(fontBold).setFontColor(ColorConstants.WHITE))
          .setBackgroundColor(COLOR_PRIMARY)
          .setTextAlignment(TextAlignment.CENTER)
          .setPadding(8);
      table.addHeaderCell(headerCell);
    }

    // Datos
    for (int i = 0; i < mortalidades.size(); i++) {
      ControlMortalidad m = mortalidades.get(i);
      Color bgColor = (i % 2 == 0) ? ColorConstants.WHITE : COLOR_LIGHT_GRAY;

      // Calcular porcentaje estimado de mortalidad (simulado)
      double porcentajeMortalidad = (m.getCantidad() * 100.0) / 1000; // Asumiendo lote de 1000

      table.addCell(crearCeldaDatos(String.valueOf(m.getId()), fontRegular, bgColor));
      table.addCell(crearCeldaDatos(m.getFecha().toString(), fontRegular, bgColor));
      table.addCell(
          crearCeldaDatos(m.getGranja().getNombre() + " (" + m.getGranja().getCodigo() + ")", fontRegular, bgColor));
      table.addCell(crearCeldaDatos(String.valueOf(m.getNumeroGalpon()), fontRegular, bgColor));
      table.addCell(crearCeldaDatos(String.valueOf(m.getCantidad()), fontRegular, bgColor));
      table.addCell(crearCeldaDatos(m.getSexo().equals("M") ? "Macho" : "Hembra", fontRegular, bgColor));
      table.addCell(crearCeldaDatos(m.getLote() != null ? m.getLote() : "N/A", fontRegular, bgColor));
      table.addCell(crearCeldaDatos(String.format("%.2f%%", porcentajeMortalidad), fontRegular, bgColor));
    }

    document.add(table);
  }

  /**
   * Crea una celda para la tabla de datos.
   */
  private static Cell crearCeldaDatos(String content, PdfFont font, Color bgColor) {
    return new Cell()
        .add(new Paragraph(content).setFont(font).setFontSize(9))
        .setBackgroundColor(bgColor)
        .setTextAlignment(TextAlignment.CENTER)
        .setPadding(6)
        .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f));
  }

  /**
   * Agrega un resumen por granja.
   */
  private static void agregarResumenPorGranja(Document document, List<ControlMortalidad> mortalidades,
      PdfFont fontBold, PdfFont fontRegular) {
    // Título de sección
    Paragraph tituloResumen = new Paragraph("RESUMEN POR GRANJA")
        .setFont(fontBold)
        .setFontSize(14)
        .setFontColor(COLOR_PRIMARY)
        .setMarginBottom(10)
        .setMarginTop(20);
    document.add(tituloResumen);

    // Agrupar por granja
    Map<String, List<ControlMortalidad>> mortalidadPorGranja = mortalidades.stream()
        .collect(Collectors.groupingBy(m -> m.getGranja().getNombre()));

    // Crear tabla resumen
    Table resumenTable = new Table(UnitValue.createPercentArray(new float[] { 40, 20, 20, 20 }))
        .setWidth(UnitValue.createPercentValue(100));

    // Encabezados
    resumenTable
        .addHeaderCell(new Cell().add(new Paragraph("Granja").setFont(fontBold).setFontColor(ColorConstants.WHITE))
            .setBackgroundColor(COLOR_ACCENT).setTextAlignment(TextAlignment.CENTER).setPadding(8));
    resumenTable
        .addHeaderCell(new Cell().add(new Paragraph("Registros").setFont(fontBold).setFontColor(ColorConstants.WHITE))
            .setBackgroundColor(COLOR_ACCENT).setTextAlignment(TextAlignment.CENTER).setPadding(8));
    resumenTable.addHeaderCell(
        new Cell().add(new Paragraph("Total Mortalidad").setFont(fontBold).setFontColor(ColorConstants.WHITE))
            .setBackgroundColor(COLOR_ACCENT).setTextAlignment(TextAlignment.CENTER).setPadding(8));
    resumenTable
        .addHeaderCell(new Cell().add(new Paragraph("Promedio").setFont(fontBold).setFontColor(ColorConstants.WHITE))
            .setBackgroundColor(COLOR_ACCENT).setTextAlignment(TextAlignment.CENTER).setPadding(8));

    // Datos por granja
    mortalidadPorGranja.forEach((granja, lista) -> {
      int totalMortalidad = lista.stream().mapToInt(ControlMortalidad::getCantidad).sum();
      double promedio = totalMortalidad / (double) lista.size();

      resumenTable.addCell(new Cell().add(new Paragraph(granja).setFont(fontRegular)).setPadding(6));
      resumenTable.addCell(new Cell().add(new Paragraph(String.valueOf(lista.size())).setFont(fontRegular))
          .setTextAlignment(TextAlignment.CENTER).setPadding(6));
      resumenTable.addCell(new Cell().add(new Paragraph(String.valueOf(totalMortalidad)).setFont(fontRegular))
          .setTextAlignment(TextAlignment.CENTER).setPadding(6));
      resumenTable.addCell(new Cell().add(new Paragraph(String.format("%.1f", promedio)).setFont(fontRegular))
          .setTextAlignment(TextAlignment.CENTER).setPadding(6));
    });

    document.add(resumenTable);
  }

  /**
   * Agrega el pie de página del reporte.
   */
  private static void agregarPiePagina(Document document, PdfFont fontRegular) {
    document.add(new Paragraph().setMarginTop(30));

    // Línea separadora usando tabla
    Table lineTable = new Table(1).setWidth(UnitValue.createPercentValue(100));
    lineTable.addCell(new Cell().setHeight(1).setBackgroundColor(COLOR_LIGHT_GRAY).setBorder(Border.NO_BORDER));
    document.add(lineTable);

    Paragraph piePagina = new Paragraph("Sistema Integrador Avícola © 2025 - Reporte generado automáticamente")
        .setFont(fontRegular)
        .setFontSize(8)
        .setFontColor(COLOR_DARK_GRAY)
        .setTextAlignment(TextAlignment.CENTER)
        .setMarginTop(10);

    document.add(piePagina);
  }
}
