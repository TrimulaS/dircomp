
![img_1.png](img_1.png)
- 

- add tableView
- Open Path in explorer
- Button cancel size
Collaps/ expand
Add RMB Item open in explorer
Process FileItiem extended to File
Edit Combobox text with single click - double click opens Directory open dialog
If the same folder selected - process duplicates only ignoring files with the same path
- caclcuate directory sizes
- scroll to text arrea Status 
add modal window to ask beforepe peramnent deltetion
switcing table view treeView
ProgressBar Comparision tracking x100
delete to recycle bin
- add tahs #delete every where file delete operation
- configure Icons with x2 x3 xN (Showing number of duplicates)
- Adding TableView decoration to highlight outstanding items
- remade content mennu for TreeView
- whe files - the first directory ignored
- During compaision save Obseved List of links to same and similar Objects
- add settings tab setup path to move items instead of delete during testnig
- Add Union intersect subtract 
- DoubleConten Meniu
- When find same in other set and selected it - scroll to it 
- sync Tree <-> Table
- add notifiacation about found same copies when selected e.g. label
Bugs:
- - fix collase/ expand Treeview
- Check why ProgressBar shows incorrect values after "Cancel"
- Double click on Toggle button cacel highliting as selected
- - add notifiacation about found same copies when selected e.g. label

        toggleButton1.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && group.getSelectedToggle() == toggleButton1) {
                toggleButton1.setSelected(true);
            }
        });


