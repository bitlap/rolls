"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[572],{3905:(e,t,r)=>{r.d(t,{Zo:()=>u,kt:()=>f});var n=r(7294);function a(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function o(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function l(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?o(Object(r),!0).forEach((function(t){a(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):o(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function s(e,t){if(null==e)return{};var r,n,a=function(e,t){if(null==e)return{};var r,n,a={},o=Object.keys(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||(a[r]=e[r]);return a}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(a[r]=e[r])}return a}var i=n.createContext({}),c=function(e){var t=n.useContext(i),r=t;return e&&(r="function"==typeof e?e(t):l(l({},t),e)),r},u=function(e){var t=c(e.components);return n.createElement(i.Provider,{value:t},e.children)},p="mdxType",d={inlineCode:"code",wrapper:function(e){var t=e.children;return n.createElement(n.Fragment,{},t)}},m=n.forwardRef((function(e,t){var r=e.components,a=e.mdxType,o=e.originalType,i=e.parentName,u=s(e,["components","mdxType","originalType","parentName"]),p=c(r),m=a,f=p["".concat(i,".").concat(m)]||p[m]||d[m]||o;return r?n.createElement(f,l(l({ref:t},u),{},{components:r})):n.createElement(f,l({ref:t},u))}));function f(e,t){var r=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var o=r.length,l=new Array(o);l[0]=m;var s={};for(var i in t)hasOwnProperty.call(t,i)&&(s[i]=t[i]);s.originalType=e,s[p]="string"==typeof e?e:a,l[1]=s;for(var c=2;c<o;c++)l[c]=r[c];return n.createElement.apply(null,l)}return n.createElement.apply(null,r)}m.displayName="MDXCreateElement"},5203:(e,t,r)=>{r.r(t),r.d(t,{assets:()=>i,contentTitle:()=>l,default:()=>d,frontMatter:()=>o,metadata:()=>s,toc:()=>c});var n=r(7462),a=(r(7294),r(3905));const o={title:"ResultSetX",custom_edit_url:"https://github.com/bitlap/rolls/edit/master/docs/resultset_x.md"},l=void 0,s={unversionedId:"resultset_x",id:"resultset_x",title:"ResultSetX",description:"java.sql.ResultSet Data Extractor",source:"@site/../rolls-docs/target/mdoc/resultset_x.md",sourceDirName:".",slug:"/resultset_x",permalink:"/docs/resultset_x",draft:!1,editUrl:"https://github.com/bitlap/rolls/edit/master/docs/resultset_x.md",tags:[],version:"current",frontMatter:{title:"ResultSetX",custom_edit_url:"https://github.com/bitlap/rolls/edit/master/docs/resultset_x.md"},sidebar:"tutorialSidebar",previous:{title:"CSV",permalink:"/docs/csv"},next:{title:"Validate Ident Prefix",permalink:"/docs/validate_ident_prefix"}},i={},c=[{value:"<code>java.sql.ResultSet</code> Data Extractor",id:"javasqlresultset-data-extractor",level:2}],u={toc:c},p="wrapper";function d(e){let{components:t,...r}=e;return(0,a.kt)(p,(0,n.Z)({},u,r,{components:t,mdxType:"MDXLayout"}),(0,a.kt)("h2",{id:"javasqlresultset-data-extractor"},(0,a.kt)("inlineCode",{parentName:"h2"},"java.sql.ResultSet")," Data Extractor"),(0,a.kt)("p",null,"If you want to quickly obtain all the data for the ",(0,a.kt)("inlineCode",{parentName:"p"},"ResultSet")," without relying on the orm framework. For example, during testing."),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-scala"},"val rowSet: ResultSet = statement.getResultSet\n// rows is a Scala Tuple\nval rows = ResultSetX[TypeRow4[Int, String, String, String]](rowSet).fetch()\nassert(rows.size == 2)\n// Scala3 Tuple to List\nassert(rows.head.values.size == 4)\n")))}d.isMDXComponent=!0}}]);