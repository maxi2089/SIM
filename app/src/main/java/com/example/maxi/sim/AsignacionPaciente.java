package com.example.maxi.sim;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yamila on 26/10/2015.
 */
public class AsignacionPaciente  extends CommonDto implements java.io.Serializable  {

        private Integer idPaciente;

        private Set<Integer> idsUsuario = new HashSet();
        private Set<Usuario> usuariosAsignados = new HashSet();
        private Set<Usuario> usuariosNoAsignados = new HashSet();

        public AsignacionPaciente() {
        }


        public Integer getIdPaciente() {
            return idPaciente;
        }


        public void setIdPaciente(Integer idPaciente) {
            this.idPaciente = idPaciente;
        }


        public Set<Integer> getIdsUsuario() {
            return idsUsuario;
        }


        public void setIdsUsuario(Set<Integer> idsUsuario) {
            this.idsUsuario = idsUsuario;
        }

        /**
         * @return the usuariosAsignados
         */
        public Set<Usuario> getUsuariosAsignados() {
            return usuariosAsignados;
        }

        /**
         * @param usuariosAsignados the usuariosAsignados to set
         */
        public void setUsuariosAsignados(Set<Usuario> usuariosAsignados) {
            this.usuariosAsignados = usuariosAsignados;
        }

        /**
         * @return the usuariosNoAsignados
         */
        public Set<Usuario> getUsuariosNoAsignados() {
            return usuariosNoAsignados;
        }

        /**
         * @param usuariosNoAsignados the usuariosNoAsignados to set
         */
        public void setUsuariosNoAsignados(Set<Usuario> usuariosNoAsignados) {
            this.usuariosNoAsignados = usuariosNoAsignados;
        }



}
