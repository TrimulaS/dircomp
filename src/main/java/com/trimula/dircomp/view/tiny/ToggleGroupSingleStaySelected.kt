package com.trimula.dircomp.view.tiny
import com.trimula.dircomp.dataprocessing.Log
import com.trimula.dircomp.model.DirectoryAnalysis
import com.trimula.dircomp.model.FileItem
import javafx.beans.value.ObservableValue
import javafx.collections.transformation.SortedList
import javafx.scene.control.TableView
import javafx.scene.control.ToggleButton
import javafx.scene.control.TreeView

class ToggleGroupSingleStaySelected(vararg buttons: ToggleButton) {
    private val toggleButtons = mutableListOf<ToggleButton>()

    init {
        // Добавляем переданные кнопки в список
        toggleButtons.addAll(buttons)
        toggleButtons.forEach { button -> setupToggleButton(button) }

        // Если все кнопки не выбраны, принудительно выбираем первую
        if (toggleButtons.none { it.isSelected }) {
            toggleButtons.first().isSelected = true
        }
    }

    // Добавить ToggleButton в группу
    fun addToggleButton(button: ToggleButton) {
        toggleButtons.add(button)
        setupToggleButton(button)
    }

    // Настройка каждого ToggleButton
    private fun setupToggleButton(button: ToggleButton) {
        button.selectedProperty().addListener { _: ObservableValue<out Boolean>, _: Boolean, isSelected: Boolean ->
            // Если все кнопки не выбраны, оставляем эту выбранной
            if (!isSelected && toggleButtons.none { it.isSelected }) {
                button.isSelected = true
            }
        }
    }




}



