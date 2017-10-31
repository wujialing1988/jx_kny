Ext.namespace("Ext.ux.layout");

Ext.ux.layout.TableFormLayout = Ext.extend(Ext.layout.TableLayout, {
    monitorResize: true,
    setContainer: function() {
        Ext.layout.FormLayout.prototype.setContainer.apply(this, arguments);
        this.currentRow = 0;
        this.currentColumn = 0;
        this.cells = [];
    },
    renderItem : function(c, position, target) {
        if (c && !c.rendered) {
            var cell = Ext.get(this.getNextCell(c));
            cell.addClass("x-table-layout-column-" + this.currentColumn);
            Ext.layout.FormLayout.prototype.renderItem.call(this, c, 0, cell);
        }
    },
    getLayoutTargetSize : Ext.layout.AnchorLayout.prototype.getLayoutTargetSize,
    getTemplateArgs : Ext.layout.FormLayout.prototype.getTemplateArgs,
    onLayout : function(ct, target) {
        Ext.ux.layout.TableFormLayout.superclass.onLayout.call(this, ct, target);
        if (!target.hasClass("x-table-form-layout-ct")) {
            target.addClass("x-table-form-layout-ct");
        }
        var viewSize = this.getLayoutTargetSize(ct, target);
        var aw, ah;
        if (ct.anchorSize) {
            if (typeof ct.anchorSize == "number") {
                aw = ct.anchorSize;
            } else {
                aw = ct.anchorSize.width;
                ah = ct.anchorSize.height;
            }
        } else {
            aw = ct.initialConfig.width;
            ah = ct.initialConfig.height;
        }

        var cs = this.getRenderedItems(ct), len = cs.length, i, c, a, cw, ch, el, vs, boxes = [];
        var x, w, h, col, colWidth, offset;
        for (i = 0; i < len; i++) {
            c = cs[i];
            // get table cell
            x = c.getEl().parent(".x-table-layout-cell");
            if (this.columnWidths) {
                // get column
                col = parseInt(x.dom.className.replace(/.*x\-table\-layout\-column\-([\d]+).*/, "$1"));
                // get cell width (based on column widths)
                colWidth = 0, offset = 0;
                for (j = col; j < (col + (c.colspan || 1)); j++) {
                    colWidth += this.columnWidths[j];
                    offset += 10;
                }
                w = (viewSize.width * colWidth) - offset;
            } else {
                // get cell width
                w = (viewSize.width / this.columns) * (c.colspan || 1);
            }
            // set table cell width
            x.setWidth(w);
            // get cell width (-10 for spacing between cells) & height to be base width of anchored component
            w = w - 10;
            h = x.getHeight();
            // If a child container item has no anchor and no specific width, set the child to the default anchor size
            if (!c.anchor && c.items && !Ext.isNumber(c.width) && !(Ext.isIE6 && Ext.isStrict)){
                c.anchor = this.defaultAnchor;
            }

            if(c.anchor){
                a = c.anchorSpec;
                if(!a){ // cache all anchor values
                    vs = c.anchor.split(' ');
                    c.anchorSpec = a = {
                        right: this.parseAnchor(vs[0], c.initialConfig.width, aw),
                        bottom: this.parseAnchor(vs[1], c.initialConfig.height, ah)
                    };
                }
                cw = a.right ? this.adjustWidthAnchor(a.right(w), c) : undefined;
                ch = a.bottom ? this.adjustHeightAnchor(a.bottom(h), c) : undefined;

                if(cw || ch){
                    boxes.push({
                        comp: c,
                        width: cw || undefined,
                        height: ch || undefined
                    });
                }
            }
        }      
        for (i = 0, len = boxes.length; i < len; i++) {
            c = boxes[i];
            c.comp.setSize(c.width, c.height);
        }      
    },
    
    parseAnchor : function(a, start, cstart) {
        if (a && a != "none") {
            var last;
            if (/^(r|right|b|bottom)$/i.test(a)) {
                var diff = cstart - start;
                return function(v) {
                    if (v !== last) {
                        last = v;
                        return v - diff;
                    }
                };
            } else if (a.indexOf("%") != -1) {
                var ratio = parseFloat(a.replace("%", "")) * .01;
                return function(v) {
                    if (v !== last) {
                        last = v;
                        return Math.floor(v * ratio);
                    }
                };
            } else {
                a = parseInt(a, 10);
                if (!isNaN(a)) {
                    return function(v) {
                        if (v !== last) {
                            last = v;
                            return v + a;
                        }
                    };
                }
            }
        }
        return false;
    },
    adjustWidthAnchor : function(value, comp) {
        return value - (comp.isFormField ? (comp.hideLabel ? 0 : this.labelAdjust) : 0);
    },
    adjustHeightAnchor : function(value, comp) {
        return value;
    },
    // private
    isValidParent : function(c, target){
        return c.getPositionEl().up('table', 6).dom.parentNode === (target.dom || target);
    },
    getLabelStyle : Ext.layout.FormLayout.prototype.getLabelStyle,
    labelSeparator : Ext.layout.FormLayout.prototype.labelSeparator,
    trackLabels: Ext.layout.FormLayout.prototype.trackLabels,
    onFieldShow: Ext.layout.FormLayout.prototype.onFieldShow,
    onFieldHide: Ext.layout.FormLayout.prototype.onFieldHide
});
Ext.Container.LAYOUTS['tableform'] = Ext.ux.layout.TableFormLayout;