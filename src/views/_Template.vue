<!--
@author: wang.weili
@since : 2020/11/12
-->
<template>
  <div class="template">
    <h1>This is an template page</h1>
  </div>
</template>

<script>
// import Com from '@/components/Com.vue'
export default {
  name: 'template',
  created(){

  },
  // components: {
  //   Com
  // },
  computed:{
    user(){
      return this.$store.state.user
    }
  },
  data(){
    return{

    }
  },
  methods:{

  }
}
</script>
