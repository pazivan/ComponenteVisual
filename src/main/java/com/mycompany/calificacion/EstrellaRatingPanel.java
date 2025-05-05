package com.mycompany.calificacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Panel de calificación con estrellas interactivas.
 * Permite a los usuarios seleccionar una calificación de 1 a 5 estrellas (o el máximo configurado).
 * Soporta imágenes personalizadas o generación de estrellas vectoriales.
 */
public class EstrellaRatingPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private int maxEstrellas = 5;
    private int calificacion = 0;
    private JLabel[] estrellas;
    private ImageIcon estrellaLlena;
    private ImageIcon estrellaVacia;
    private int tamanoEstrella = 30;
    private Color colorEstrella = Color.YELLOW;

    /**
     * Constructor que inicializa el panel de calificación.
     */
    public EstrellaRatingPanel() {
        initComponents();
    }

    /**
     * Inicializa los componentes del panel.
     */
    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        setOpaque(false);
        
        // Cargar imágenes
        cargarImagenes();
        
        // Crear interfaz de estrellas
        crearEstrellasUI();
    }

    /**
     * Carga las imágenes de estrellas llenas y vacías desde los recursos.
     * Si no encuentra las imágenes, usará gráficos vectoriales.
     */
    private void cargarImagenes() {
        try {
            // Cargar imágenes desde recursos
            estrellaLlena = cargarImagen("/estrella11.png");
            estrellaVacia = cargarImagen("/estrella22.png");
            
            // Escalar si las imágenes se cargaron correctamente
            if (estrellaLlena != null && estrellaVacia != null) {
                estrellaLlena = escalarIcono(estrellaLlena, tamanoEstrella);
                estrellaVacia = escalarIcono(estrellaVacia, tamanoEstrella);
            } else {
                System.err.println("No se encontraron las imágenes de estrellas. Usando gráficos vectoriales.");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar imágenes: " + e.getMessage());
        }
    }

    /**
     * Carga una imagen desde la ruta especificada.
     * @param ruta Ruta relativa de la imagen en los recursos
     * @return ImageIcon con la imagen cargada, o null si no se encontró
     */
    private ImageIcon cargarImagen(String ruta) {
        try {
            java.net.URL imgURL = getClass().getResource(ruta);
            if (imgURL != null) {
                return new ImageIcon(imgURL);
            } else {
                System.err.println("No se encontró la imagen: " + ruta);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error al cargar imagen: " + e.getMessage());
            return null;
        }
    }

    /**
     * Escala un icono al tamaño especificado.
     * @param icono Icono original a escalar
     * @param tamano Tamaño deseado (ancho y alto)

* @return Nuevo ImageIcon escalado
     */
    private ImageIcon escalarIcono(ImageIcon icono, int tamano) {
        Image img = icono.getImage();
        Image newImg = img.getScaledInstance(tamano, tamano, Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }

    /**
     * Crea la interfaz de usuario con las estrellas interactivas.
     */
    private void crearEstrellasUI() {
        estrellas = new JLabel[maxEstrellas];
        
        for (int i = 0; i < maxEstrellas; i++) {
            final int index = i;
            JLabel estrella = new JLabel();
            
            // Configurar icono inicial
            if (estrellaVacia != null) {
                estrella.setIcon(estrellaVacia);
            } else {
                estrella.setIcon(crearEstrellaGrafica(false));
            }
            
            // Configurar eventos
            estrella.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            estrella.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    setCalificacion(index + 1);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    resaltarEstrellas(index + 1);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    resaltarEstrellas(calificacion);
                }
            });
            
            estrellas[i] = estrella;
            add(estrella);
        }
    }

    /**
     * Crea una estrella gráfica vectorial.
     * @param llena true para estrella llena, false para solo el contorno
     * @return ImageIcon con la estrella generada
     */
    private ImageIcon crearEstrellaGrafica(boolean llena) {
        BufferedImage img = new BufferedImage(tamanoEstrella, tamanoEstrella, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Coordenadas para forma de estrella
        int[] xPoints = {15, 18, 24, 19, 21, 15, 9, 11, 6, 12};
        int[] yPoints = {0, 9, 9, 15, 24, 18, 24, 15, 9, 9};
        
        g.setColor(colorEstrella);
        if (llena) {
            g.fillPolygon(xPoints, yPoints, xPoints.length);
        } else {
            g.drawPolygon(xPoints, yPoints, xPoints.length);
        }
        g.dispose();
        return new ImageIcon(img);
    }

    /**
     * Establece la calificación actual.
     * @param calificacion Valor entre 0 y maxEstrellas
     */
    public void setCalificacion(int calificacion) {
        this.calificacion = Math.max(0, Math.min(calificacion, maxEstrellas));
        actualizarEstrellas();
    }

    /**
     * Obtiene la calificación actual.
     * @return Valor de la calificación (0 a maxEstrellas)
     */
    public int getCalificacion() {
        return calificacion;
    }

    /**
     * Establece el número máximo de estrellas.
     * @param maxEstrellas Número de estrellas (mínimo 1)
     */
    public void setMaxEstrellas(int maxEstrellas) {
        this.maxEstrellas = Math.max(1, maxEstrellas);
        removeAll();
        crearEstrellasUI();
        revalidate();
        repaint();
    }

    /**
     * Obtiene el número máximo de estrellas.
     * @return Número máximo de estrellas configurado
     */
    public int getMaxEstrellas() {
        return maxEstrellas;
    }

    /**
     * Establece el tamaño de las estrellas.
     * @param tamano Tamaño en píxeles (entre 10 y 100)
     */
    public void setTamanoEstrella(int tamano) {
        this.tamanoEstrella = Math.max(10, Math.min(tamano, 100));
        
        // Re-escalar imágenes si existen
        if (estrellaLlena != null && estrellaVacia != null) {
            estrellaLlena = escalarIcono(new ImageIcon(estrellaLlena.getImage()), tamanoEstrella);
            estrellaVacia = escalarIcono(new ImageIcon(estrellaVacia.getImage()), tamanoEstrella);
        }
        
        actualizarEstrellas();
    }

    /**
     * Obtiene el tamaño actual de las estrellas.
     * @return Tamaño en píxeles
     */
    public int getTamanoEstrella() {
        return tamanoEstrella;
    }

    /**
     * Establece el color de las estrellas (solo aplica a estrellas vectoriales).
     * @param color Color deseado
     */
    public void setColorEstrella(Color color) {
        this.colorEstrella = color;
        // Solo afecta a las estrellas gráficas
        if (estrellaLlena == null || estrellaVacia == null) {
            actualizarEstrellas();
        }
    }

    /**
     * Obtiene el color actual de las estrellas.
     * @return Color actual
     */
    public Color getColorEstrella() {
        return colorEstrella;
    }

    /**
     * Actualiza la visualización de las estrellas según la calificación actual.
     */
    private void actualizarEstrellas() {
        for (int i = 0; i < estrellas.length; i++) {
            if (i < calificacion) {
                estrellas[i].setIcon(estrellaLlena != null ? estrellaLlena : crearEstrellaGrafica(true));
            } else {
                estrellas[i].setIcon(estrellaVacia != null ? estrellaVacia : crearEstrellaGrafica(false));
            }
        }
    }

    /**
     * Resalta las estrellas hasta el índice especificado (efecto hover).
     * @param hasta Índice de la última estrella a resaltar
     */
    private void resaltarEstrellas(int hasta) {
        for (int i = 0; i < estrellas.length; i++) {
            if (i < hasta) {
                estrellas[i].setIcon(estrellaLlena != null ? estrellaLlena : crearEstrellaGrafica(true));
            } else {
                estrellas[i].setIcon(estrellaVacia != null ? estrellaVacia : crearEstrellaGrafica(false));
            }
        }
    }
}