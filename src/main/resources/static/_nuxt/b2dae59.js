(window.webpackJsonp=window.webpackJsonp||[]).push([[15,22],{510:function(e,t,n){var content=n(516);content.__esModule&&(content=content.default),"string"==typeof content&&(content=[[e.i,content,""]]),content.locals&&(e.exports=content.locals);(0,n(15).default)("50788f08",content,!0,{sourceMap:!1})},516:function(e,t,n){var r=n(14)(!1);r.push([e.i,".v-autocomplete.v-input>.v-input__control>.v-input__slot{cursor:text}.v-autocomplete input{align-self:center}.v-autocomplete.v-select.v-input--is-focused input{min-width:64px}.v-autocomplete:not(.v-input--is-focused).v-select--chips input{max-height:0;padding:0}.v-autocomplete--is-selecting-index input{opacity:0}.v-autocomplete.v-text-field--enclosed:not(.v-text-field--solo):not(.v-text-field--single-line):not(.v-text-field--outlined) .v-select__slot>input{margin-top:24px}.v-autocomplete.v-text-field--enclosed:not(.v-text-field--solo):not(.v-text-field--single-line):not(.v-text-field--outlined).v-input--dense .v-select__slot>input{margin-top:20px}.v-autocomplete:not(.v-input--is-disabled).v-select.v-text-field input{pointer-events:inherit}.v-autocomplete__content.v-menu__content,.v-autocomplete__content.v-menu__content .v-card{border-radius:0}",""]),e.exports=r},528:function(e,t,n){"use strict";n(10),n(8),n(12),n(9),n(13);var r=n(1),o=(n(66),n(11),n(68),n(105),n(32),n(46),n(510),n(517)),l=n(482),c=n(58),h=n(0);function d(object,e){var t=Object.keys(object);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(object);e&&(n=n.filter((function(e){return Object.getOwnPropertyDescriptor(object,e).enumerable}))),t.push.apply(t,n)}return t}function f(e){for(var i=1;i<arguments.length;i++){var source=null!=arguments[i]?arguments[i]:{};i%2?d(Object(source),!0).forEach((function(t){Object(r.a)(e,t,source[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(source)):d(Object(source)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(source,t))}))}return e}var v=f(f({},o.b),{},{offsetY:!0,offsetOverflow:!0,transition:!1});t.a=o.a.extend({name:"v-autocomplete",props:{allowOverflow:{type:Boolean,default:!0},autoSelectFirst:{type:Boolean,default:!1},filter:{type:Function,default:function(e,t,n){return n.toLocaleLowerCase().indexOf(t.toLocaleLowerCase())>-1}},hideNoData:Boolean,menuProps:{type:o.a.options.props.menuProps.type,default:function(){return v}},noFilter:Boolean,searchInput:{type:String}},data:function(){return{lazySearch:this.searchInput}},computed:{classes:function(){return f(f({},o.a.options.computed.classes.call(this)),{},{"v-autocomplete":!0,"v-autocomplete--is-selecting-index":this.selectedIndex>-1})},computedItems:function(){return this.filteredItems},selectedValues:function(){var e=this;return this.selectedItems.map((function(t){return e.getValue(t)}))},hasDisplayedItems:function(){var e=this;return this.hideSelected?this.filteredItems.some((function(t){return!e.hasItem(t)})):this.filteredItems.length>0},currentRange:function(){return null==this.selectedItem?0:String(this.getText(this.selectedItem)).length},filteredItems:function(){var e=this;return!this.isSearching||this.noFilter||null==this.internalSearch?this.allItems:this.allItems.filter((function(t){var n=Object(h.r)(t,e.itemText),text=null!=n?String(n):"";return e.filter(t,String(e.internalSearch),text)}))},internalSearch:{get:function(){return this.lazySearch},set:function(e){this.lazySearch!==e&&(this.lazySearch=e,this.$emit("update:search-input",e))}},isAnyValueAllowed:function(){return!1},isDirty:function(){return this.searchIsDirty||this.selectedItems.length>0},isSearching:function(){return this.multiple&&this.searchIsDirty||this.searchIsDirty&&this.internalSearch!==this.getText(this.selectedItem)},menuCanShow:function(){return!!this.isFocused&&(this.hasDisplayedItems||!this.hideNoData)},$_menuProps:function(){var e=o.a.options.computed.$_menuProps.call(this);return e.contentClass="v-autocomplete__content ".concat(e.contentClass||"").trim(),f(f({},v),e)},searchIsDirty:function(){return null!=this.internalSearch&&""!==this.internalSearch},selectedItem:function(){var e=this;return this.multiple?null:this.selectedItems.find((function(i){return e.valueComparator(e.getValue(i),e.getValue(e.internalValue))}))},listData:function(){var data=o.a.options.computed.listData.call(this);return data.props=f(f({},data.props),{},{items:this.virtualizedItems,noFilter:this.noFilter||!this.isSearching||!this.filteredItems.length,searchInput:this.internalSearch}),data}},watch:{filteredItems:"onFilteredItemsChanged",internalValue:"setSearch",isFocused:function(e){e?(document.addEventListener("copy",this.onCopy),this.$refs.input&&this.$refs.input.select()):(document.removeEventListener("copy",this.onCopy),this.blur(),this.updateSelf())},isMenuActive:function(e){!e&&this.hasSlot&&(this.lazySearch=null)},items:function(e,t){t&&t.length||!this.hideNoData||!this.isFocused||this.isMenuActive||!e.length||this.activateMenu()},searchInput:function(e){this.lazySearch=e},internalSearch:"onInternalSearchChanged",itemText:"updateSelf"},created:function(){this.setSearch()},destroyed:function(){document.removeEventListener("copy",this.onCopy)},methods:{onFilteredItemsChanged:function(e,t){var n=this;e!==t&&(this.setMenuIndex(-1),this.$nextTick((function(){n.internalSearch&&(1===e.length||n.autoSelectFirst)&&(n.$refs.menu.getTiles(),n.setMenuIndex(0))})))},onInternalSearchChanged:function(){this.updateMenuDimensions()},updateMenuDimensions:function(){this.isMenuActive&&this.$refs.menu&&this.$refs.menu.updateDimensions()},changeSelectedIndex:function(e){this.searchIsDirty||(this.multiple&&e===h.y.left?-1===this.selectedIndex?this.selectedIndex=this.selectedItems.length-1:this.selectedIndex--:this.multiple&&e===h.y.right?this.selectedIndex>=this.selectedItems.length-1?this.selectedIndex=-1:this.selectedIndex++:e!==h.y.backspace&&e!==h.y.delete||this.deleteCurrentItem())},deleteCurrentItem:function(){var e=this.selectedIndex,t=this.selectedItems[e];if(this.isInteractive&&!this.getDisabled(t)){var n=this.selectedItems.length-1;if(-1!==this.selectedIndex||0===n){var r=e!==this.selectedItems.length-1?e:e-1;this.selectedItems[r]?this.selectItem(t):this.setValue(this.multiple?[]:null),this.selectedIndex=r}else this.selectedIndex=n}},clearableCallback:function(){this.internalSearch=null,o.a.options.methods.clearableCallback.call(this)},genInput:function(){var input=l.a.options.methods.genInput.call(this);return input.data=Object(c.a)(input.data,{attrs:{"aria-activedescendant":Object(h.p)(this.$refs.menu,"activeTile.id"),autocomplete:Object(h.p)(input.data,"attrs.autocomplete","off")},domProps:{value:this.internalSearch}}),input},genInputSlot:function(){var slot=o.a.options.methods.genInputSlot.call(this);return slot.data.attrs.role="combobox",slot},genSelections:function(){return this.hasSlot||this.multiple?o.a.options.methods.genSelections.call(this):[]},onClick:function(e){this.isInteractive&&(this.selectedIndex>-1?this.selectedIndex=-1:this.onFocus(),this.isAppendInner(e.target)||this.activateMenu())},onInput:function(e){if(!(this.selectedIndex>-1)&&e.target){var t=e.target,n=t.value;t.value&&this.activateMenu(),this.internalSearch=n,this.badInput=t.validity&&t.validity.badInput}},onKeyDown:function(e){var t=e.keyCode;!e.ctrlKey&&[h.y.home,h.y.end].includes(t)||o.a.options.methods.onKeyDown.call(this,e),this.changeSelectedIndex(t)},onSpaceDown:function(e){},onTabDown:function(e){o.a.options.methods.onTabDown.call(this,e),this.updateSelf()},onUpDown:function(e){e.preventDefault(),this.activateMenu()},selectItem:function(e){o.a.options.methods.selectItem.call(this,e),this.setSearch()},setSelectedItems:function(){o.a.options.methods.setSelectedItems.call(this),this.isFocused||this.setSearch()},setSearch:function(){var e=this;this.$nextTick((function(){e.multiple&&e.internalSearch&&e.isMenuActive||(e.internalSearch=!e.selectedItems.length||e.multiple||e.hasSlot?null:e.getText(e.selectedItem))}))},updateSelf:function(){(this.searchIsDirty||this.internalValue)&&(this.multiple||this.valueComparator(this.internalSearch,this.getValue(this.internalValue))||this.setSearch())},hasItem:function(e){return this.selectedValues.indexOf(this.getValue(e))>-1},onCopy:function(e){var t,n;if(-1!==this.selectedIndex){var r=this.selectedItems[this.selectedIndex],o=this.getText(r);null==(t=e.clipboardData)||t.setData("text/plain",o),null==(n=e.clipboardData)||n.setData("text/vnd.vuetify.autocomplete.item+plain",o),e.preventDefault()}}}})},529:function(e,t,n){"use strict";var r=n(106),o=n(35),l=n(5),c=n(7);t.a=Object(l.a)(r.a,o.a).extend({name:"v-hover",props:{disabled:{type:Boolean,default:!1},value:{type:Boolean,default:void 0}},methods:{onMouseEnter:function(){this.runDelay("open")},onMouseLeave:function(){this.runDelay("close")}},render:function(){return this.$scopedSlots.default||void 0!==this.value?(this.$scopedSlots.default&&(element=this.$scopedSlots.default({hover:this.isActive})),Array.isArray(element)&&1===element.length&&(element=element[0]),element&&!Array.isArray(element)&&element.tag?(this.disabled||(element.data=element.data||{},this._g(element.data,{mouseenter:this.onMouseEnter,mouseleave:this.onMouseLeave})),element):(Object(c.c)("v-hover should only contain a single element",this),element)):(Object(c.c)("v-hover is missing a default scopedSlot or bound value",this),null);var element}})},530:function(e,t,n){var content=n(531);content.__esModule&&(content=content.default),"string"==typeof content&&(content=[[e.i,content,""]]),content.locals&&(e.exports=content.locals);(0,n(15).default)("7f6d4ad6",content,!0,{sourceMap:!1})},531:function(e,t,n){var r=n(14)(!1);r.push([e.i,".theme--light.v-pagination .v-pagination__item{background:#fff;color:rgba(0,0,0,.87)}.theme--light.v-pagination .v-pagination__item--active{color:#fff}.theme--light.v-pagination .v-pagination__navigation{background:#fff}.theme--dark.v-pagination .v-pagination__item{background:#1e1e1e;color:#fff}.theme--dark.v-pagination .v-pagination__item--active{color:#fff}.theme--dark.v-pagination .v-pagination__navigation{background:#1e1e1e}.v-pagination{align-items:center;display:inline-flex;list-style-type:none;justify-content:center;margin:0;max-width:100%;width:100%}.v-pagination.v-pagination{padding-left:0}.v-pagination>li{align-items:center;display:flex}.v-pagination--circle .v-pagination__item,.v-pagination--circle .v-pagination__more,.v-pagination--circle .v-pagination__navigation{border-radius:50%}.v-pagination--disabled{pointer-events:none;opacity:.6}.v-pagination__item{background:transparent;border-radius:4px;font-size:1rem;height:34px;margin:.3rem;min-width:34px;padding:0 5px;text-decoration:none;transition:.3s cubic-bezier(0,0,.2,1);width:auto;box-shadow:0 3px 1px -2px rgba(0,0,0,.2),0 2px 2px 0 rgba(0,0,0,.14),0 1px 5px 0 rgba(0,0,0,.12)}.v-pagination__item--active{box-shadow:0 2px 4px -1px rgba(0,0,0,.2),0 4px 5px 0 rgba(0,0,0,.14),0 1px 10px 0 rgba(0,0,0,.12)}.v-pagination__navigation{box-shadow:0 3px 1px -2px rgba(0,0,0,.2),0 2px 2px 0 rgba(0,0,0,.14),0 1px 5px 0 rgba(0,0,0,.12);border-radius:4px;display:inline-flex;justify-content:center;align-items:center;text-decoration:none;height:32px;width:32px;margin:.3rem 10px}.v-pagination__navigation .v-icon{transition:.2s cubic-bezier(.4,0,.6,1);vertical-align:middle}.v-pagination__navigation--disabled{opacity:.6;pointer-events:none}.v-pagination__more{margin:.3rem;display:inline-flex;align-items:flex-end;justify-content:center;height:32px;width:32px}",""]),e.exports=r},537:function(e,t,n){"use strict";n.r(t);var r={props:["page"],data:function(){return{currentPage:this.$route.query.page?parseInt(this.$route.query.page):1}},computed:{totalPages:function(){return console.log("page in pagination",this.page),this.page&&"mediaListNew"==this.page?(this.$store.state.campaignMedia&&this.$store.state.campaignMedia.totalPages&&console.log("toatal pages",this.$store.state.campaignMedia.totalPages),this.$store.state.campaignMedia&&this.$store.state.campaignMedia.totalPages):this.page&&"mediaList"==this.page?(this.$store.state.media&&this.$store.state.media.totalPages&&console.log("toatal pages",this.$store.state.media.totalPages),this.$store.state.media&&this.$store.state.media.totalPages):null}},methods:{clicked:function(e){this.currentPage=e,this.$emit("changePage",e),this.$router.push("?page=".concat(e))}},watch:{$route:function(e,t){"/"===e.fullPath&&(console.log("watching in pagination"),this.currentPage=1)}}},o=n(45),l=n(56),c=n.n(l),h=n(580),component=Object(o.a)(r,(function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"text-center page"},[e.totalPages&&e.totalPages?n("v-pagination",{staticClass:"custom",attrs:{length:e.totalPages&&e.totalPages,circle:""},on:{input:e.clicked},model:{value:e.currentPage,callback:function(t){e.currentPage=t},expression:"currentPage"}}):e._e(),e._v(" "),e.totalPages&&e.totalPages?n("div",{staticClass:"caption grey--text"},[e._v("\n    Page "+e._s(e.currentPage)+" of "+e._s(e.totalPages&&e.totalPages)+"\n  ")]):e._e()],1)}),[],!1,null,null,null);t.default=component.exports;c()(component,{VPagination:h.a})},575:function(e,t,n){var content=n(634);content.__esModule&&(content=content.default),"string"==typeof content&&(content=[[e.i,content,""]]),content.locals&&(e.exports=content.locals);(0,n(15).default)("4056fce6",content,!0,{sourceMap:!1})},580:function(e,t,n){"use strict";n(10),n(8),n(11),n(12),n(9),n(13);var r=n(57),o=n(1),l=(n(19),n(27),n(47),n(16),n(72),n(66),n(530),n(88)),c=n(107),h=n(23),d=n(241),f=n(17),v=n(5);function m(object,e){var t=Object.keys(object);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(object);e&&(n=n.filter((function(e){return Object.getOwnPropertyDescriptor(object,e).enumerable}))),t.push.apply(t,n)}return t}t.a=Object(v.a)(h.a,Object(d.a)({onVisible:["init"]}),f.a).extend({name:"v-pagination",directives:{Resize:c.a},props:{circle:Boolean,disabled:Boolean,length:{type:Number,default:0,validator:function(e){return e%1==0}},nextIcon:{type:String,default:"$next"},prevIcon:{type:String,default:"$prev"},totalVisible:[Number,String],value:{type:Number,default:0},pageAriaLabel:{type:String,default:"$vuetify.pagination.ariaLabel.page"},currentPageAriaLabel:{type:String,default:"$vuetify.pagination.ariaLabel.currentPage"},previousAriaLabel:{type:String,default:"$vuetify.pagination.ariaLabel.previous"},nextAriaLabel:{type:String,default:"$vuetify.pagination.ariaLabel.next"},wrapperAriaLabel:{type:String,default:"$vuetify.pagination.ariaLabel.wrapper"}},data:function(){return{maxButtons:0,selected:null}},computed:{classes:function(){return function(e){for(var i=1;i<arguments.length;i++){var source=null!=arguments[i]?arguments[i]:{};i%2?m(Object(source),!0).forEach((function(t){Object(o.a)(e,t,source[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(source)):m(Object(source)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(source,t))}))}return e}({"v-pagination":!0,"v-pagination--circle":this.circle,"v-pagination--disabled":this.disabled},this.themeClasses)},items:function(){var e=parseInt(this.totalVisible,10);if(0===e)return[];var t=Math.min(Math.max(0,e)||this.length,Math.max(0,this.maxButtons)||this.length,this.length);if(this.length<=t)return this.range(1,this.length);var n=t%2==0?1:0,o=Math.floor(t/2),l=this.length-o+1+n;if(this.value>o&&this.value<l){var c=this.length,h=this.value-o+2,d=this.value+o-2-n,f=d+1===c-1?d+1:"...";return[1,h-1==2?2:"..."].concat(Object(r.a)(this.range(h,d)),[f,this.length])}if(this.value===o){var v=this.value+o-1-n;return[].concat(Object(r.a)(this.range(1,v)),["...",this.length])}if(this.value===l){var m=this.value-o+1;return[1,"..."].concat(Object(r.a)(this.range(m,this.length)))}return[].concat(Object(r.a)(this.range(1,o)),["..."],Object(r.a)(this.range(l,this.length)))}},watch:{value:function(){this.init()}},mounted:function(){this.init()},methods:{init:function(){var e=this;this.selected=null,this.$nextTick(this.onResize),setTimeout((function(){return e.selected=e.value}),100)},onResize:function(){var e=this.$el&&this.$el.parentElement?this.$el.parentElement.clientWidth:window.innerWidth;this.maxButtons=Math.floor((e-96)/42)},next:function(e){e.preventDefault(),this.$emit("input",this.value+1),this.$emit("next")},previous:function(e){e.preventDefault(),this.$emit("input",this.value-1),this.$emit("previous")},range:function(e,t){for(var n=[],i=e=e>0?e:1;i<=t;i++)n.push(i);return n},genIcon:function(e,t,n,r,label){return e("li",[e("button",{staticClass:"v-pagination__navigation",class:{"v-pagination__navigation--disabled":n},attrs:{disabled:n,type:"button","aria-label":label},on:n?{}:{click:r}},[e(l.a,[t])])])},genItem:function(e,i){var t=this,n=i===this.value&&(this.color||"primary"),r=i===this.value,o=r?this.currentPageAriaLabel:this.pageAriaLabel;return e("button",this.setBackgroundColor(n,{staticClass:"v-pagination__item",class:{"v-pagination__item--active":i===this.value},attrs:{type:"button","aria-current":r,"aria-label":this.$vuetify.lang.t(o,i)},on:{click:function(){return t.$emit("input",i)}}}),[i.toString()])},genItems:function(e){var t=this;return this.items.map((function(i,n){return e("li",{key:n},[isNaN(Number(i))?e("span",{class:"v-pagination__more"},[i.toString()]):t.genItem(e,i)])}))},genList:function(e,t){return e("ul",{directives:[{modifiers:{quiet:!0},name:"resize",value:this.onResize}],class:this.classes},t)}},render:function(e){var t=[this.genIcon(e,this.$vuetify.rtl?this.nextIcon:this.prevIcon,this.value<=1,this.previous,this.$vuetify.lang.t(this.previousAriaLabel)),this.genItems(e),this.genIcon(e,this.$vuetify.rtl?this.prevIcon:this.nextIcon,this.value>=this.length,this.next,this.$vuetify.lang.t(this.nextAriaLabel))];return e("nav",{attrs:{role:"navigation","aria-label":this.$vuetify.lang.t(this.wrapperAriaLabel)}},[this.genList(e,t)])}})},633:function(e,t,n){"use strict";n(575)},634:function(e,t,n){var r=n(14)(!1);r.push([e.i,".pagination[data-v-44ef7f99]{position:absolute;display:flex;justify-content:center;bottom:0;width:100%}.text-ellipsis[data-v-44ef7f99]{white-space:nowrap;word-break:normal;overflow:hidden;text-overflow:ellipsis}.card-sensor[data-v-44ef7f99]{overflow:hidden;cursor:pointer;position:relative}.ico-sensor[data-v-44ef7f99]{position:absolute;font-size:150px;top:6px;right:-40px;transition:transform .5s ease}.ico-actions[data-v-44ef7f99]{position:absolute;bottom:0;right:0;z-index:10}",""]),e.exports=r},637:function(e,t,n){"use strict";n.r(t);n(8),n(12),n(13);var r=n(57),o=n(3),l=n(1),c=(n(41),n(11),n(319),n(9),n(10),n(27),n(78));function h(object,e){var t=Object.keys(object);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(object);e&&(n=n.filter((function(e){return Object.getOwnPropertyDescriptor(object,e).enumerable}))),t.push.apply(t,n)}return t}function d(e){for(var i=1;i<arguments.length;i++){var source=null!=arguments[i]?arguments[i]:{};i%2?h(Object(source),!0).forEach((function(t){Object(l.a)(e,t,source[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(source)):h(Object(source)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(source,t))}))}return e}var f={props:["selectedCampaign","isMediaPopUp"],components:{PaginationBar:n(537).default},data:function(){return{dialogDelete:!1,mediaDataID:null,card:"",mediaTypeValue:null,id:null,isChangeColor:null,selectedIndex:null,searchMedia:"",loading:!1,sort:"id",sortDirection:"dec",pageNumber:1,itemsPerPage:8,mediaSchema:{id:"",assetCode:"",vendorAssetCode:"",ownedByOrgId:"",status:"",length:"",width:"",height:"",orientation:"",captureFrequency:"",illumination:"",latitude:"",longitude:"",road:"",locality:"",city:"",district:"",state:"",pincode:"",cityTier:null,mediaClass:"",structureType:"",mediaType:"",placeType:"",material:"",placementType:"",catchmentStrata:"",locationType:"",trafficType:"",trafficDensity:"",trafficSignal:"",ageGroup:"",viewingDistance:"",viewingTime:"",quality:"",managedByStartDate:"",managedByEndDate:"",managedByOrgId:""},tableFields:[],selectedFields:[],chipFields:[],chipFilterObj:{}}},created:function(){this.$store.dispatch("media/fetchMedia")},computed:d({},Object(c.c)({userPrivileges:function(e){return e.userPrivileges},mediaData:function(e){return e.campaignMedia.mediaDataByCampaign},isMediaSearching:function(e){return e.campaignMedia.isMediaSearching}})),methods:{onPageChange:function(e){console.log("listening to emit",e),e>0&&(this.pageNumber=e),this.triggerApi()},appendCamapiagnId:function(e){return console.log("inside appendCamapiagnId"),this.selectedCampaign&&this.selectedCampaign.id&&(e.searchData=d(d({},e.searchData),{},{campaignId:String(this.selectedCampaign.id)})),e},cardClick:function(e,t){e.id&&e.assetCode?(this.mediaTypeValue=e.assetCode,this.id=e.id,this.selectedIndex=t,console.log("index",this.selectedIndex)):console.log(" id or media value not found")},addMedia:function(){var e={mediaTypeValue:this.mediaTypeValue,mediaId:this.id};e.mediaTypeValue?this.$emit("getInfo",e):(console.log("no data"),this.$showErrorToast(this,"Please select Media"))},update:function(data){this.$router.push({name:"media-view-id",params:{id:data.id}})},navigate:function(data){this.$router.push({name:"media-view-block-id",params:{id:data.id}})},deleteMediaModal:function(data){this.dialogDelete=!0,this.mediaDataID=data.id},deleteMedia:function(){var e=this;null!=this.mediaDataID&&(this.$store.dispatch("media/deleteMediaData",this.mediaDataID).then((function(){e.$showSuccessToast(e,"Deleted Successfully")})).catch((function(){e.$showErrorToast(e,"Error !")})),this.dialogDelete=!1)},resetData:function(){this.selectedIndex=null},closePopup:function(){this.$emit("closeMediaPopup")},updateSelectedFields:function(e){this.chipFields=this.chipFields.filter((function(t){return t.field!==e.field})),this.selectedFields=this.selectedFields.filter((function(t){return t.field!==e.field}))},trigerFilter:function(e){e&&13==e.keyCode&&this.triggerApi()},preparePayload:function(){var e={searchData:{},pageNo:1,pageSize:this.itemsPerPage||8};if(this.sort&&(e.sortField=this.sort),this.sortDirection&&(e.sortOrder=this.sortDirection),this.chipFields&&this.chipFields.length){var t={};this.chipFields.forEach((function(e){e=JSON.parse(JSON.stringify(e)),t[e.field]=e.value})),e.searchData=t}return e},setTableFields:function(){var e=this;this.mediaSchema&&Object.keys(this.mediaSchema).length&&Object.keys(this.mediaSchema).forEach((function(t){e.tableFields.push({field:t,searchKey:""})}))},onChangeFilter:_.debounce((function(){this.triggerApi()}),1500),triggerApi:function(){var e=this;return Object(o.a)(regeneratorRuntime.mark((function t(){var n,r;return regeneratorRuntime.wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return console.log("triggerApi"),e.resetData(),n={searchData:{},pageNo:e.pageNumber||1,pageSize:e.itemsPerPage||8},e.sort&&(n.sortField=e.sort),e.sortDirection&&(n.sortOrder=e.sortDirection),t.next=7,e.grabChipFilter();case 7:return e.chipFilterObj&&Object.keys(e.chipFilterObj).length&&(n.searchData=d(d({},n.searchData),e.chipFilterObj)),t.next=10,e.appendCamapiagnId(n);case 10:(r=t.sent)&&r.searchData&&r.searchData.campaignId?(console.log("paylopad",r),e.$store.dispatch("campaignMedia/fetchMediaListByCampaignId",r)):console.log("Error :: select camapaign");case 12:case"end":return t.stop()}}),t)})))()},grabChipFilter:function(){if(this.chipFields&&this.chipFields.length){var e={};this.chipFields.forEach((function(t){t=JSON.parse(JSON.stringify(t)),e[t.field]=t.searchKey})),this.chipFilterObj=e}else this.chipFilterObj={}}},watch:{mediaData:function(e){console.log("mediadata state",e),this.loading=!1},isMediaPopUp:function(e){e&&(console.log("trigger api"),this.triggerApi())},isMediaSearching:function(e){this.loading=e},selectedFields:function(e){var t=this;if(e){var n=e.filter((function(e){var n=e.field;return!t.chipFields.some((function(e){return e.field===n}))})),o=this.chipFields.filter((function(t){var n=t.field;return!e.some((function(e){return e.field===n}))}));if(n&&n.length&&(this.chipFields=[].concat(Object(r.a)(this.chipFields),Object(r.a)(n))),o&&o.length){var l=this.chipFields.filter((function(e){var t=e.field;return!o.some((function(e){return e.field===t}))}));this.chipFields=l}this.chipFields=JSON.parse(JSON.stringify(this.chipFields))}},isSerching:function(e){this.loading=e},chipFields:function(e){e&&!e.length&&this.triggerApi()}},mounted:function(){this.resetData(),this.triggerApi(),this.setTableFields()}},v=(n(633),n(45)),m=n(56),y=n.n(m),x=n(528),I=n(234),S=n(224),O=n(99),w=n(591),D=n(496),C=n(504),P=n(529),j=n(220),F=n(225),$=n(501),k=n(502),M=n(482),component=Object(v.a)(f,(function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",[n("v-card",{attrs:{color:"primary",flat:"",dark:""}},[n("v-card-title",[n("v-btn",{staticClass:"mr-2",attrs:{icon:"",dark:""},on:{click:e.closePopup}},[n("v-icon",[e._v("mdi-close")])],1),e._v("\n      Media Report "),n("v-spacer"),e._v(" "),n("v-autocomplete",{staticClass:"mr-6",attrs:{label:"Add Filter",items:e.tableFields,required:"",multiple:"","return-object":"","item-text":"field","item-value":"field",color:"white",dark:"","z-index":"10"},model:{value:e.selectedFields,callback:function(t){e.selectedFields=t},expression:"selectedFields"}}),e._v(" "),n("v-spacer"),e._v(" "),n("v-btn",{staticClass:"black--text mr-4",attrs:{color:"white"},on:{click:e.addMedia}},[e._v("SELECT")])],1)],1),e._v(" "),n("v-card",{attrs:{dark:"",color:"primary",flat:""}},[e.chipFields&&e.chipFields.length?n("div",e._l(e.chipFields,(function(t,r){return n("v-chip",{key:r,staticClass:"ma-2",attrs:{close:"",color:"rgba(255,255,255,.15)"},on:{"click:close":function(n){return e.updateSelectedFields(t)}}},[e._v("\n        "+e._s(t.field)+" :\n        "),n("v-text-field",{attrs:{label:"value",flat:"","single-line":"",width:"50px"},on:{keydown:e.trigerFilter,input:e.onChangeFilter},model:{value:t.searchKey,callback:function(n){e.$set(t,"searchKey",n)},expression:"item.searchKey"}})],1)})),1):e._e()]),e._v(" "),e.mediaData&&!e.loading?n("v-row",{attrs:{justify:"start",align:"center"}},e._l(e.mediaData,(function(t,r){return n("v-col",{key:r,attrs:{col:"12",xs:"12",sm:"6",md:"4",lg:"3"}},[n("v-hover",{attrs:{"open-delay":"200"},scopedSlots:e._u([{key:"default",fn:function(o){var l=o.hover;return[n("v-card",{staticClass:"card-sensor",class:{"on-hover":l},style:r==e.selectedIndex?"border: 5px solid red":"",attrs:{height:"200px",elevation:l?16:2},on:{click:function(n){return e.cardClick(t,r)}},model:{value:e.card,callback:function(t){e.card=t},expression:"card"}},[n("div",{staticClass:"d-flex flex-no-wrap justify-space-between"},[n("div",[n("v-card-title",{staticClass:"text-h5 text-ellipsis font-weight-bold"},[e._v("\n                "+e._s(t.locality?t.locality+", ":"-")+"\n                "+e._s(t.city?t.city:"")+"\n              ")]),e._v(" "),n("v-card-subtitle",{staticClass:"py-1 pb-0 font-weight-bold"},[e._v("\n                "+e._s(t.assetCode?t.assetCode:"-")+"\n              ")]),e._v(" "),n("v-card-subtitle",{staticClass:"py-1"},[e._v("\n                "+e._s(t.mediaType?t.mediaType.value:"-")+"\n              ")]),e._v(" "),n("v-card-subtitle",{staticClass:"py-1"},[e._v("\n                "+e._s(t.material?t.material.value:"-")+"\n              ")]),e._v(" "),n("v-card-subtitle",{staticClass:"py-1"},[e._v("\n                "+e._s("H: "+(t.height?t.height:"-"))+",\n                "+e._s("W: "+(t.width?t.width:"-"))+"\n              ")]),e._v(" "),n("v-card-subtitle",{staticClass:"py-1"},[e._v("\n                "+e._s(t.ownedByOrgId?t.ownedByOrgId.name:"-")+"\n              ")])],1)])])]}}],null,!0)})],1)})),1):e._e(),e._v(" "),n("h3",{directives:[{name:"show",rawName:"v-show",value:!(e.loading||e.mediaData&&e.mediaData.length),expression:"!loading && !(mediaData && mediaData.length)"}],staticClass:"py-5 text-center font-weight-bold primary--text"},[e._v("\n    No data available\n  ")]),e._v(" "),e.loading?n("h3",{staticClass:"py-5 text-center font-weight-bold primary--text"},[n("v-container",{staticStyle:{height:"400px"}},[n("v-row",{staticClass:"fill-height",attrs:{"align-content":"center",justify:"center"}},[n("v-col",{staticClass:"text-subtitle-1 text-center font-weight-bold",attrs:{cols:"12"}},[e._v("\n          Getting Your Media..\n        ")]),e._v(" "),n("v-col",{attrs:{cols:"6"}},[n("v-progress-linear",{attrs:{color:"primary accent-4",indeterminate:"",rounded:"",height:"6"}})],1)],1)],1)],1):e._e(),e._v(" "),n("div",{staticClass:"pagination"},[n("PaginationBar",{attrs:{page:"mediaListNew"},on:{changePage:e.onPageChange}})],1)],1)}),[],!1,null,"44ef7f99",null);t.default=component.exports;y()(component,{VAutocomplete:x.a,VBtn:I.a,VCard:S.a,VCardSubtitle:O.b,VCardTitle:O.d,VChip:w.a,VCol:D.a,VContainer:C.a,VHover:P.a,VIcon:j.a,VProgressLinear:F.a,VRow:$.a,VSpacer:k.a,VTextField:M.a})}}]);