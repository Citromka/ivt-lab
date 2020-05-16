package hu.bme.mit.spaceship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.spi.ToolProvider;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GT4500Test {

    private GT4500 ship;
    private TorpedoStore mockPrimary;
    private TorpedoStore mockSecondary;

    @BeforeEach
    public void init() {
        this.mockPrimary = mock(TorpedoStore.class);
        this.mockSecondary = mock(TorpedoStore.class);
        this.ship = new GT4500(mockPrimary, mockSecondary);
    }

    @Test
    public void fireTorpedo_Single_Success() {
        // Arrange
        when(mockPrimary.fire(1)).thenReturn(true);

        // Act
        boolean result = ship.fireTorpedo(FiringMode.SINGLE);

        // Assert
        assertTrue(result);
        verify(mockPrimary, times(1)).fire(1);
    }

    @Test
    public void fireTorpedo_All_Success() {
        // Arrange
        when(mockPrimary.fire(1)).thenReturn(true);
        when(mockSecondary.fire(1)).thenReturn(true);

        // AcassertEquals(true, result);t
        boolean result = ship.fireTorpedo(FiringMode.ALL);

        // Assert

        verify(mockPrimary, times(1)).fire(1);
        verify(mockSecondary, times(1)).fire(1);
    }

    @Test
    public void fireTorpedo_Single_Alternating_Success() {
        // Arrange
        when(mockPrimary.fire(1)).thenReturn(true);
        when(mockSecondary.fire(1)).thenReturn(true);

        // Act
        boolean first = ship.fireTorpedo(FiringMode.SINGLE);
        boolean second = ship.fireTorpedo(FiringMode.SINGLE);

        // Assert
        assertTrue(first);
        assertTrue(second);
        InOrder inOrder = inOrder(mockPrimary, mockSecondary);
        inOrder.verify(mockPrimary).fire(1);
        inOrder.verify(mockSecondary).fire(1);

    }

    @Test
    public void fireTorpedo_Single_WhenSecondIsEmpty() {
        // Arrange
        when(mockPrimary.fire(1)).thenReturn(true);
        when(mockSecondary.isEmpty()).thenReturn(true);

        // Act
        boolean first = ship.fireTorpedo(FiringMode.SINGLE);
        boolean second = ship.fireTorpedo(FiringMode.SINGLE);

        // Assert
        assertTrue(first);
        assertTrue(second);
        verify(mockPrimary, times(2)).fire(1);
        verify(mockSecondary, never()).fire(1);
    }

    @Test
    public void fireTorpedo_Single_WhenPrimaryHasOne() {
        // Arrange
        when(mockPrimary.isEmpty()).thenReturn(false, true);
        when(mockPrimary.fire(1)).thenReturn(true);
        when(mockSecondary.fire(1)).thenReturn(true);

        // Act
        boolean first = ship.fireTorpedo(FiringMode.SINGLE);
        boolean second = ship.fireTorpedo(FiringMode.SINGLE);
        boolean third = ship.fireTorpedo(FiringMode.SINGLE);

        // Assert
        assertTrue(first);
        assertTrue(second);
        assertTrue(third);
        InOrder inOrder = inOrder(mockPrimary, mockSecondary);
        inOrder.verify(mockPrimary).fire(1);
        inOrder.verify(mockSecondary).fire(1);
        inOrder.verify(mockSecondary).fire(1);
    }

    @Test
    public void fireTorpedo_Single_WhenPrimaryFails() {
        // Arrange
        when(mockPrimary.fire(1)).thenReturn(false);
        when(mockSecondary.fire(1)).thenReturn(true);

        boolean first = ship.fireTorpedo(FiringMode.SINGLE);

        assertFalse(first);
        verify(mockPrimary, times(1)).fire(1);
        verify(mockSecondary, never()).fire(1);
    }

    @Test
    public void fireTorpedo_Sinlge_WhenBothIsEmpty() {
        when(mockPrimary.isEmpty()).thenReturn(true);
        when(mockSecondary.isEmpty()).thenReturn(true);

        boolean first = ship.fireTorpedo(FiringMode.SINGLE);

        assertFalse(first);
        verify(mockPrimary, never()).fire(1);
        verify(mockSecondary, never()).fire(1);
    }

    @Test
    public void fireTorpedo_All_WhenBothIsEmpty() {
        when(mockPrimary.isEmpty()).thenReturn(true);
        when(mockSecondary.isEmpty()).thenReturn(true);

        boolean first = ship.fireTorpedo(FiringMode.ALL);

        assertFalse(first);
        verify(mockPrimary, never()).fire(1);
        verify(mockSecondary, never()).fire(1);
    }

    @Test
    public void fireLaser_IsNotPossible() {

        boolean single = ship.fireLaser(FiringMode.SINGLE);
        boolean all = ship.fireLaser(FiringMode.ALL);

        assertFalse(single);
        assertFalse(all);
    }

    @Test
    public void fireTorpedo_Single_WhenPrimaryEmpty() {
        when(mockPrimary.isEmpty()).thenReturn(false, true);
        when(mockSecondary.isEmpty()).thenReturn(true);
        when(mockPrimary.fire(1)).thenReturn(true);

        boolean first = ship.fireTorpedo(FiringMode.SINGLE);
        boolean second = ship.fireTorpedo(FiringMode.SINGLE);

        assertTrue(first);
        assertFalse(second);
        verify(mockPrimary, times(1)).fire(1);
        verify(mockSecondary, never()).fire(1);
    }
}
