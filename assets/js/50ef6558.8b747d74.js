"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[745],{3905:(e,t,r)=>{r.d(t,{Zo:()=>p,kt:()=>f});var a=r(7294);function n(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function l(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,a)}return r}function o(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?l(Object(r),!0).forEach((function(t){n(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):l(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function i(e,t){if(null==e)return{};var r,a,n=function(e,t){if(null==e)return{};var r,a,n={},l=Object.keys(e);for(a=0;a<l.length;a++)r=l[a],t.indexOf(r)>=0||(n[r]=e[r]);return n}(e,t);if(Object.getOwnPropertySymbols){var l=Object.getOwnPropertySymbols(e);for(a=0;a<l.length;a++)r=l[a],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(n[r]=e[r])}return n}var s=a.createContext({}),c=function(e){var t=a.useContext(s),r=t;return e&&(r="function"==typeof e?e(t):o(o({},t),e)),r},p=function(e){var t=c(e.components);return a.createElement(s.Provider,{value:t},e.children)},u="mdxType",m={inlineCode:"code",wrapper:function(e){var t=e.children;return a.createElement(a.Fragment,{},t)}},d=a.forwardRef((function(e,t){var r=e.components,n=e.mdxType,l=e.originalType,s=e.parentName,p=i(e,["components","mdxType","originalType","parentName"]),u=c(r),d=n,f=u["".concat(s,".").concat(d)]||u[d]||m[d]||l;return r?a.createElement(f,o(o({ref:t},p),{},{components:r})):a.createElement(f,o({ref:t},p))}));function f(e,t){var r=arguments,n=t&&t.mdxType;if("string"==typeof e||n){var l=r.length,o=new Array(l);o[0]=d;var i={};for(var s in t)hasOwnProperty.call(t,s)&&(i[s]=t[s]);i.originalType=e,i[u]="string"==typeof e?e:n,o[1]=i;for(var c=2;c<l;c++)o[c]=r[c];return a.createElement.apply(null,o)}return a.createElement.apply(null,r)}d.displayName="MDXCreateElement"},4990:(e,t,r)=>{r.r(t),r.d(t,{assets:()=>s,contentTitle:()=>o,default:()=>m,frontMatter:()=>l,metadata:()=>i,toc:()=>c});var a=r(7462),n=(r(7294),r(3905));const l={title:"Quick Start",custom_edit_url:"https://github.com/bitlap/rolls/edit/master/docs/quickstart.md"},o=void 0,i={unversionedId:"quickstart",id:"quickstart",title:"Quick Start",description:"This guide gets you started with rolls with a simple working example.",source:"@site/../rolls-docs/target/mdoc/quickstart.md",sourceDirName:".",slug:"/quickstart",permalink:"/rolls/docs/quickstart",draft:!1,editUrl:"https://github.com/bitlap/rolls/edit/master/docs/quickstart.md",tags:[],version:"current",frontMatter:{title:"Quick Start",custom_edit_url:"https://github.com/bitlap/rolls/edit/master/docs/quickstart.md"},sidebar:"tutorialSidebar",next:{title:"classSchema Annotation",permalink:"/rolls/docs/classSchema_annotation"}},s={},c=[{value:"Prerequisites",id:"prerequisites",level:2},{value:"Get the example code",id:"get-the-example-code",level:2}],p={toc:c},u="wrapper";function m(e){let{components:t,...r}=e;return(0,n.kt)(u,(0,a.Z)({},p,r,{components:t,mdxType:"MDXLayout"}),(0,n.kt)("p",null,"This guide gets you started with rolls with a simple working example."),(0,n.kt)("h2",{id:"prerequisites"},"Prerequisites"),(0,n.kt)("ul",null,(0,n.kt)("li",{parentName:"ul"},(0,n.kt)("a",{parentName:"li",href:"https://jdk.java.net"},"JDK")," version 8 or higher"),(0,n.kt)("li",{parentName:"ul"},(0,n.kt)("a",{parentName:"li",href:"https://www.scala-lang.org/"},"Scala")," 3.2.0 or higher")),(0,n.kt)("h2",{id:"get-the-example-code"},"Get the example code"),(0,n.kt)("p",null,"The example code is part of the ",(0,n.kt)("a",{parentName:"p",href:"https://github.com/bitlap/rolls"},"rolls")," repository."),(0,n.kt)("ol",null,(0,n.kt)("li",{parentName:"ol"},(0,n.kt)("p",{parentName:"li"},"clone the repo:"),(0,n.kt)("pre",{parentName:"li"},(0,n.kt)("code",{parentName:"pre",className:"language-bash"},"git clone https://github.com/bitlap/rolls\n"))),(0,n.kt)("li",{parentName:"ol"},(0,n.kt)("p",{parentName:"li"},"Take a look at ",(0,n.kt)("inlineCode",{parentName:"p"},"rolls-plugin-tests")," or the following unit tests:"),(0,n.kt)("pre",{parentName:"li"},(0,n.kt)("code",{parentName:"pre",className:"language-bash"},"rolls/rolls-core/src/test/scala\nrolls/rolls-csv/src/test/scala\n")))))}m.isMDXComponent=!0}}]);