/**
 * Typy narzędzi dostępnych w edytorze graficznym.
 * Określają aktualny tryb pracy aplikacji (rysowanie lub edycja).
 */
public enum ToolType {
    
    /** 
     * Narzędzie do rysowania prostokątów. 
     */
    RECTANGLE,
    
    /** 
     * Narzędzie do rysowania okręgów. 
     */
    CIRCLE,
    
    /** 
     * Narzędzie do rysowania wielokątów. 
     */
    POLYGON,
    
    /** 
     * Narzędzie do zaznaczania, przesuwania, obracania i zmiany kolorów figur. 
     */
    EDIT
}